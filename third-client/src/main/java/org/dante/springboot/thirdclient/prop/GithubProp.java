package org.dante.springboot.thirdclient.prop;

import lombok.Data;

/**
 * Github OAuth 配置类
 * 
 * https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps
 * 
 * @author dante
 *
 */
@Data
public class GithubProp {

	/**
	 * 客户端ClientId（在服务端注册时获取）
	 */
	private String clientId;
	/**
	 * 客户端ClientSecret（在服务端注册时获取）
	 */
	private String clientSecret;
	/**
	 * 客户端回调Uri
	 */
	private String redirectUri;
	/**
	 * 服务端授权码接口
	 */
	private String authorizeUri;
	/**
	 * 服务端访问令牌接口
	 */
	private String accessTokenUri;
	/**
	 * 客户端在服务端的权限码（在服务端注册后获取）
	 */
	private String scope;
	/**
	 * 服务端资源 API 接口
	 */
	private String authApiServerUri;
}
