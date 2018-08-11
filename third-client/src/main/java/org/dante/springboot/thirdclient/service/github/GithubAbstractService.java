package org.dante.springboot.thirdclient.service.github;

import javax.annotation.PostConstruct;

import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.service.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Access Token: df32b69247ece0244a88ca07b68635d80f26b9bf
 * 
 * @author dante
 *
 */
@Slf4j
public abstract class GithubAbstractService {
	
	protected static WebClient tokenWebClient;
	protected static WebClient apiWebClient;
	
	@Autowired
	protected JedisClient jedisClient;
	@Autowired
	protected SpiritProperties spiritProperties;
	
	@PostConstruct
	public void init() {
		if(tokenWebClient == null) {
			log.info("初始化 Github Token WebClient...");
			tokenWebClient = WebClient.create(spiritProperties.getGithub().getAccessTokenUri());
		}
		if(apiWebClient == null) {
			log.info("初始化 Github API WebClient...");
			apiWebClient = WebClient.create(spiritProperties.getGithub().getAuthApiServerUri());
		}
	}
	
	/**
	 * 构造 API 请求 Header
	 * 
	 * @return
	 * @throws OAuthException
	 */
	protected String buildReqHeader() throws OAuthException {
		var val = jedisClient.getString(OAuthConsts.GITHUB_ACCESS_TOKEN);
		if(StringUtils.isEmpty(val)) {
			throw new OAuthException("Token 已经过期，请重新申请! ");
		}
		return "token ".concat(val);
	}
}
