package org.dante.springboot.server.vo;

import lombok.Data;

/**
 * 本地认证 VO
 * 
 * @author dante
 *
 */
@Data
public class AuthorizeCodeVO {
	private String userName;
	private String password;
	private String clientId;
	private String redirectUri;
	private String scope;
	private String state;
	
}
