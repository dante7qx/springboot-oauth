package org.dante.springboot.server.vo;

import lombok.Data;

/**
 * 获取 AccessToken 请求类
 * 
 * @author dante
 *
 */
@Data
public class AuthorizeTokenReqVO {
	private String code;
	private String grantType = "authorization_code";	// 授权方式 - 授权码方式
	private String clientSecret;
	private String clientId;
	private String redirectUri;
}
