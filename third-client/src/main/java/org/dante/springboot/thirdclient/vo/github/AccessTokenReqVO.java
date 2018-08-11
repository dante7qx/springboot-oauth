package org.dante.springboot.thirdclient.vo.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessTokenReqVO {
	@JsonProperty(value = "client_id")
	private String clientId;
	@JsonProperty(value = "client_secret")
	private String clientSecret;
	private String code;
	@JsonProperty(value = "redirect_uri")
	private String redirectUri;
	private String state;
}
