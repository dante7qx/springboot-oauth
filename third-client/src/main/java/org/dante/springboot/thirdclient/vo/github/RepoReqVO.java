package org.dante.springboot.thirdclient.vo.github;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RepoReqVO {

	private int page = 1;
	@JsonProperty(value = "per_page")
	private int perPage = 100;
	
	public Map<String, Integer> param() {
		return Map.of("page", this.page, "perPage", this.perPage);
	}
}
