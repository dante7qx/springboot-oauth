package org.dante.springboot.server.config;

import org.dante.springboot.server.interceptor.OAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Bean
	public OAuthInterceptor oAuthInterceptor() {
		return new OAuthInterceptor();
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/oauth/login").setViewName("oauth/authorize");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(oAuthInterceptor()).excludePathPatterns("/oauth/**");
	}
	
}
