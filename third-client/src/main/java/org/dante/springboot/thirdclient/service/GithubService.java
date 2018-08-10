package org.dante.springboot.thirdclient.service;

import javax.annotation.PostConstruct;

import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.vo.dante.AccessTokenReqVO;
import org.dante.springboot.thirdclient.vo.dante.AccessTokenRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private SpiritProperties spiritProperties;
	private WebClient tokenWebClient;
	private WebClient apiWebClient;
	
	@PostConstruct
	public void init() {
		if(tokenWebClient == null) {
			tokenWebClient = WebClient.create(spiritProperties.getGithub().getAccessTokenUri());
		}
		if(apiWebClient == null) {
			apiWebClient = WebClient.create(spiritProperties.getGithub().getAuthApiServerUri());
		}
	}
	
	/**
	 * 校验回调 Url 接收的参数
	 * 
	 * @return
	 */
	public String checkCallbackParam(String code) {
		var msg = new StringBuilder();
		if(StringUtils.isEmpty(code)) {
			msg.append("授权码为空，非法！");
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
		AccessTokenRespVO accessTokenResp = tokenWebClient.post()
			.uri("")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.syncBody(accessTokenReq)
			.retrieve()
			.bodyToMono(AccessTokenRespVO.class)
			.block();
		var accessToken = accessTokenResp.getAccessToken();
		var tokenType = accessTokenResp.getTokenType();
		jedisClient.saveString(OAuthConsts.DANTE_ACCESS_TOKEN, accessToken.concat("_").concat(tokenType));
	}
	
	/**
	 * 构造 API 请求 Header
	 * 
	 * @return
	 * @throws OAuthException
	 */
	private String buildReqHeader() throws OAuthException {
		var val = jedisClient.getString(OAuthConsts.GITHUB_ACCESS_TOKEN);
		if(StringUtils.isEmpty(val)) {
			throw new OAuthException("Token 已经过期，请重新申请! ");
		}
		var arr = val.split("_");
//		return "token ".concat(arr[0]);
		return arr[1].concat(" ").concat(arr[0]);
	}
}
