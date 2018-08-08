package org.dante.springboot.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dante.springboot.server.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuthInterceptor implements HandlerInterceptor {
	
	@Autowired
	private AuthorizeService authorizeService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String param = request.getHeader("Authorization");
		log.info("Request param ============> {}", param);
		if(StringUtils.isEmpty(param)) {
			return false;
		}
		String accessToken = param.split("\\s+")[1];
		if(authorizeService.checkAccessToken(accessToken)) {
			return true;
		}
		request.getRequestDispatcher("/oauth/login").forward(request, response);
		return false;
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
