package org.dante.springboot.thirdclient.vo.dante;

import lombok.Data;

@Data
public class AccessTokenRespVO {
	
	private String accessToken;
	private String tokenType;
	private int expiresIn;
	private String refreshToken;
	
	
}
