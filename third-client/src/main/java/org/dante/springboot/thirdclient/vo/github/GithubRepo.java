package org.dante.springboot.thirdclient.vo.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubRepo {
	
	private String name;
	
	@JsonProperty(value = "url")
	private String apiUrl;
	
	@JsonProperty(value = "html_url")
	private String htmlUrl;
	
	private String description;
	
	@JsonProperty(value = "clone_url")
	private String cloneUrl;
	
	private String size;
	
	private String language;
	
	public String getSize() {
		var tmp = Integer.parseInt(this.size);
		return tmp >= 1024 ? (tmp / 1024)+"MB" : this.size.concat("KB");
	}
}
