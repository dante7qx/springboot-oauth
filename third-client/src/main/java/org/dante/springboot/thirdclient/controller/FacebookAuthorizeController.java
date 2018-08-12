package org.dante.springboot.thirdclient.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.prop.FacebookProp;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.service.JedisClient;
import org.dante.springboot.thirdclient.service.facebook.FacebookOAuthService;
import org.dante.springboot.thirdclient.vo.facebook.AccessTokenReqVO;
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
@RequestMapping(("/facebook"))
public class FacebookAuthorizeController {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private SpiritProperties spiritProperties;
	@Autowired
	private FacebookOAuthService facebookOAuthService;
	
	/**
	 * Facebook 主页
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/home")
	public String home(Model model) {
		try {
			String accessToken = jedisClient.getString(OAuthConsts.FACEBOOK_ACCESS_TOKEN);
			model.addAttribute("accessToken", accessToken);
		} catch (Exception e) {
			log.error("findFacebookResource error.", e);
			return "redirect:/";
		}
		return "facebook/index";
	}
	
	/**
	 * 请求资源服务器的授权码 Code
	 * 
	 * @return
	 */
	@GetMapping("/oauth/authorize")
	public String authorize() {
		var accessToken = jedisClient.getString(OAuthConsts.FACEBOOK_ACCESS_TOKEN);
		if(!StringUtils.isEmpty(accessToken)) {
			return "redirect:/facebook/home";
		}
		var facebook = spiritProperties.getFacebook();
		var serverAuthorizeUri = facebook.getAuthorizeUri()
				.concat("?")
				.concat("response_type=code")
				.concat("&client_id=").concat(facebook.getAppId())
				.concat("&redirect_uri=").concat(facebook.getRedirectUri())
				.concat("&scope=").concat(facebook.getScope())
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
	@GetMapping("/oauth/callback")
	public String authorizeCallback(@RequestParam("code") String code, 
			@RequestParam(name = "state", required = false, defaultValue = "") String state) {
		log.info("Facebook Resource Server invoke Client callback url, Authozation Code is {}", code);
		FacebookProp facebook = spiritProperties.getFacebook();
		String clientId = facebook.getAppId();
		String redirectUri = facebook.getRedirectUri();
		var checkMsg = facebookOAuthService.checkCallbackParam(code);
		if(!StringUtils.isEmpty(checkMsg)) {
			return checkMsg;
		}
		AccessTokenReqVO accessTokenReq = new AccessTokenReqVO();
		accessTokenReq.setClientId(clientId);
		accessTokenReq.setClientSecret(facebook.getAppSecret());
		accessTokenReq.setCode(code);
		accessTokenReq.setRedirectUri(redirectUri);
		accessTokenReq.setState(state);
		try {
			facebookOAuthService.applyAccessToken(accessTokenReq);
		} catch (Exception e) {
			log.error("获取AccessToken error", e);
			return e.getMessage();
		}
		
		return "redirect:/fackbook/home";
	}
	
}
