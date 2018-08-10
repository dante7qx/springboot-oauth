package org.dante.springboot.thirdclient.service;

import javax.annotation.PostConstruct;

import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.vo.github.AccessTokenReqVO;
import org.dante.springboot.thirdclient.vo.github.AccessTokenRespVO;
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
	 * 获取 Github Resource Server 访问令牌
	 * 将 AccessToken 存储到 Redis 中
	 * 
	 * @param accessTokenReq
	 * @return
	 * @throws OAuthException 
	 */
	public void applyAccessToken(AccessTokenReqVO accessTokenReq) throws OAuthException {
		AccessTokenRespVO accessTokenResp = tokenWebClient.post()
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.syncBody(accessTokenReq)
			.retrieve()
			.bodyToMono(AccessTokenRespVO.class)
			.block();
		if(StringUtils.isEmpty(accessTokenResp.getError())) {
			jedisClient.saveString(OAuthConsts.GITHUB_ACCESS_TOKEN, accessTokenResp.getAccessToken());
		} else {
			throw new OAuthException("从 Github 获取 Token 失败 [" + accessTokenResp.getErrorDescription() + "]");
		}
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
		return "token ".concat(val);
	}
}
