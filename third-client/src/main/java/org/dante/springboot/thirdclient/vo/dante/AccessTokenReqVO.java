package org.dante.springboot.thirdclient.vo.dante;

import lombok.Data;

@Data
public class AccessTokenReqVO {

	private String code;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String state;
	
}
