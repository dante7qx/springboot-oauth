package org.dante.springboot.thirdclient.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.service.DanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/dante")
public class DanteAuthorizeController {

	@Autowired
	private SpiritProperties spiritProperties;
	@Autowired
	private DanteService danteService;
	
	/**
	 * 但丁主页
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/home")
	public String home(Model model) {
		
		return "dante/index";
	}

	/**
	 * 请求资源服务器的授权码 Code
	 * 
	 * @return
	 */
	@GetMapping("/oauth/authorize")
	public String authorize() {
		var dante = spiritProperties.getDante();
		var serverAuthorizeUri = dante.getAuthorizeCodeUri()
				.concat("?")
				.concat("response_type=code")
				.concat("&client_id=").concat(dante.getClientId())
				.concat("&redirect_uri=").concat(dante.getRedirectUri())
				.concat("&scope=").concat(dante.getScope())
				.concat("&state=").concat(RandomStringUtils.randomAlphabetic(6).toLowerCase());
		return "redirect:".concat(serverAuthorizeUri);
	}
	
	/**
	 * 客户端回调Url
	 * 
	 * @param code
	 * @param clientId
	 * @param redirectUri
	 * @param state
	 * @return
	 */
	public String authorizeCallback(@RequestParam("code") String code, @RequestParam("client_id") String clientId,
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam(name = "state", required = false, defaultValue = "") String state) {
		
		log.info("Resource Server invoke Client callback url, Authozation Code is {}", code);
		var checkMsg = danteService.checkCallbackParam(code, clientId, redirectUri);
		if(!StringUtils.isEmpty(checkMsg)) {
			return checkMsg;
		}
		
		return "redirect:/dante/home";
	}
	
	
	
}
