package org.dante.springboot.thirdclient.vo.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ErrorMsgVO {
	private String code;
	private String message;
	private String type;
	@JsonProperty(value = "fbtrace_id")
	private String fbtraceId;
}
