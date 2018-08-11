package org.dante.springboot.thirdclient.service.dante;

import java.util.List;

import javax.annotation.PostConstruct;

import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.service.JedisClient;
import org.dante.springboot.thirdclient.vo.dante.AccessTokenReqVO;
import org.dante.springboot.thirdclient.vo.dante.AccessTokenRespVO;
import org.dante.springboot.thirdclient.vo.dante.DanteUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DanteService {
	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private SpiritProperties spiritProperties;
	private WebClient tokenWebClient;
	private WebClient apiWebClient;
	
	@PostConstruct
	public void init() {
		if(tokenWebClient == null) {
			tokenWebClient = WebClient.create(spiritProperties.getDante().getAccessTokenUri());
		}
		if(apiWebClient == null) {
			apiWebClient = WebClient.create(spiritProperties.getDante().getAuthApiServerUri());
		}
	}
	
	/**
	 * 校验回调 Url 接收的参数
	 * 
	 * @return
	 */
	public String checkCallbackParam(String code, String clientId, String redirectUri) {
		var msg = new StringBuilder();
		var dante = spiritProperties.getDante();
		if(StringUtils.isEmpty(code)) {
			msg.append("授权码为空，非法！");
		}
		
		if(!dante.getClientId().equals(clientId)) {
			msg.append("非法的ClientId！");
		}
		
		if(!dante.getRedirectUri().equals(redirectUri)) {
			msg.append("非法的RedirectUri！");
		}
		return msg.toString();
	}
	
	/**
	 * 获取但丁 Resource Server 访问令牌
	 * 将 AccessToken 和 RefreshToken 存储到 Redis 中
	 * 
	 * @param accessTokenReq
	 * @return
	 * @throws OAuthException 
	 */
	public void applyAccessToken(AccessTokenReqVO accessTokenReq) throws OAuthException {
		ResponseEntity<AccessTokenRespVO> resp = tokenWebClient.post()
			.uri("")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.syncBody(accessTokenReq)
			.exchange()
			.flatMap(response -> response.toEntity(AccessTokenRespVO.class))
			.block();
		if(HttpStatus.INTERNAL_SERVER_ERROR.equals(resp.getStatusCode())) {
			throw new OAuthException(resp.getHeaders().getFirst("ErrorMsg"));
		}
		AccessTokenRespVO accessTokenResp = resp.getBody();
		var accessToken = accessTokenResp.getAccessToken();
		var refreshToken = accessTokenResp.getRefreshToken();
		var expireIn = accessTokenResp.getExpiresIn();
		var tokenType = accessTokenResp.getTokenType();
		jedisClient.saveString(OAuthConsts.DANTE_ACCESS_TOKEN, accessToken.concat("_").concat(tokenType), expireIn);
		if (!StringUtils.isEmpty(refreshToken)) {
			jedisClient.saveString(OAuthConsts.DANTE_REFRESH_TOKEN, refreshToken, expireIn * 2);
		}
	}
	
	/**
	 * 获取但丁 Resource Server 中的用户资源
	 * 
	 * @return
	 * @throws OAuthException 
	 */
	public List<DanteUserVO> findDanteServerUsers() throws OAuthException {
		String headerParam = buildReqHeader();
		List<DanteUserVO> users = apiWebClient.get()
				.uri("/user/list")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.header(OAuthConsts.HEADER_KEY, headerParam)
				.retrieve()
				.bodyToFlux(DanteUserVO.class)
				.collectList()
				.block();
		return users;
	}
	
	/**
	 * 构造 API 请求 Header
	 * 
	 * @return
	 * @throws OAuthException
	 */
	private String buildReqHeader() throws OAuthException {
		var val = jedisClient.getString(OAuthConsts.DANTE_ACCESS_TOKEN);
		if(StringUtils.isEmpty(val)) {
			throw new OAuthException("Token 已经过期，请重新申请! ");
		}
		var arr = val.split("_");
		return arr[1].concat(" ").concat(arr[0]);
	}
	
}
