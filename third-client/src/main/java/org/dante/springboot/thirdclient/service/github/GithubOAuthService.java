package org.dante.springboot.thirdclient.service.github;

import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.vo.github.AccessTokenReqVO;
import org.dante.springboot.thirdclient.vo.github.AccessTokenRespVO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GithubOAuthService extends GithubAbstractService {

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
	
}
