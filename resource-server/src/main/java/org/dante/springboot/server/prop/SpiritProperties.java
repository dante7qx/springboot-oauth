package org.dante.springboot.server.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="spirit")
public class SpiritProperties {
	private int expireTime = 3600; // 过期时间，单位：秒。默认是 3600秒
	private int codeExpireTime = 10;	// 授权码过期时间，单位：分钟。默认是10分钟
}
