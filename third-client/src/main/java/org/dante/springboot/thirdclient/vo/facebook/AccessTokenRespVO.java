package org.dante.springboot.thirdclient.vo.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessTokenRespVO {

	@JsonProperty(value = "token_type")
	private String tokenType;
	@JsonProperty(value = "expires_in")
	private int expiresIn;
	@JsonProperty(value = "access_token")
	private String accessToken;
	
	private ErrorMsgVO error;
	
}
