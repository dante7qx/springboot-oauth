package org.dante.springboot.thirdclient.vo.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubUser {
	
	private String id;
	private String login;
	@JsonProperty(value= "avatar_url")
	private String avatarUrl;
	@JsonProperty(value = "created_at")
	private String createAt;
	@JsonProperty(value = "updated_at")
	private String updatedAt;
	
	public String getCreateAt() {
		return this.createAt.replaceAll("T", " ").replaceAll("Z", "");
	}
	
	public String getUpdatedAt() {
		return this.updatedAt.replaceAll("T", " ").replaceAll("Z", "");
	}
}
