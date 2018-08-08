package org.dante.springboot.thirdclient.service;

import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DanteService {
	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private SpiritProperties spiritProperties;
	
	/**
	 * 校验回调 Url 接收的参数
	 * 
	 * @return
	 */
	public String checkCallbackParam(String code, String clientId, String redirectUri) {
		var msg = new StringBuilder();
		var dante = spiritProperties.getDante();
		if(StringUtils.isEmpty(code)) {
			msg.append("授权码为空，非法！");
		}
		
		if(!dante.getClientId().equals(clientId)) {
			msg.append("非法的ClientId！");
		}
		
		if(dante.getRedirectUri().equals(redirectUri)) {
			msg.append("非法的RedirectUri！");
		}
		return msg.toString();
	}
	
}
