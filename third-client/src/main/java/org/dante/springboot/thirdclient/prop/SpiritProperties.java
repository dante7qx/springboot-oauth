package org.dante.springboot.thirdclient.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spirit.oauth")
public class SpiritProperties {
	/**
	 * 但丁 服务端配置
	 */
	private DanteProp dante;
	
	/**
	 * Github 服务端配置
	 */
	private GithubProp github;
	
}
