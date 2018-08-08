package org.dante.springboot.server.vo;

import lombok.Data;

@Data
public class AuthorizeTokenRespVO {
	
	private String accessToken;
	private String tokenType = "bearer";
	private int expiresIn = 3600;	// 过期时间（单位：秒）
	private String refreshToken;
	private String scope;
}
