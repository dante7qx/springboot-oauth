package org.dante.springboot.thirdclient.prop;

import lombok.Data;

/**
 * Facebook OAuth 配置类
 * 
 * https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
 * 
 * @author dante
 *
 */
@Data
public class FacebookProp {

	/**
	 * 客户端应用AppId（在服务端注册时获取）
	 */
	private String appId;
	/**
	 * 客户端应用密钥（在服务端注册时获取）
	 */
	private String appSecret;
	
	private String clientSecret; 
	/**
	 * 客户端回调Uri
	 */
	private String redirectUri;
	/**
	 * 客户端在服务端的权限码（在服务端注册后获取）
	 */
	private String scope;
	/**
	 * 服务端授权码接口
	 */
	private String authorizeUri;
	/**
	 * 服务端访问令牌接口
	 */
	private String accessTokenUri;
	
}
