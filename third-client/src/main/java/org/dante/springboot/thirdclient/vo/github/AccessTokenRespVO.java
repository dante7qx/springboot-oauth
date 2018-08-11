package org.dante.springboot.thirdclient.vo.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessTokenRespVO {

	@JsonProperty(value = "token_type")
	private String tokenType;
	private String scope;
	@JsonProperty(value = "access_token")
	private String accessToken;
	
	/**
	 * 错误时，Github 服务器返回
	 */
	private String error;
	
	@JsonProperty(value = "error_description")
	private String errorDescription;
	
	@JsonProperty(value = "error_uri")
	private String errorUri;
}
