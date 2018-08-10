package org.dante.springboot.thirdclient.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.dante.springboot.thirdclient.prop.GithubProp;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.service.GithubService;
import org.dante.springboot.thirdclient.vo.dante.AccessTokenReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/github")
public class GithubAuthorizeController {
	
	@Autowired
	private SpiritProperties spiritProperties;
	@Autowired
	private GithubService githubService;
	
	/**
	 * Github 主页
	 * 
	 * @param model
	 * @return
	 */
	/*
	@GetMapping("/home")
	public String home(Model model) {
		try {
			List<DanteUserVO> users = danteService.findDanteServerUsers();
			model.addAttribute("users", users);
		} catch (OAuthException e) {
			log.error("findDanteServerUsers error.", e);
			return "redirect:/";
		}
		return "github/index";
	}
	*/
	@GetMapping("/home")
	public @ResponseBody String home() {
		return spiritProperties.getGithub().toString();
	}
	
	/**
	 * 请求资源服务器的授权码 Code
	 * 
	 * @return
	 */
	@GetMapping("/oauth/authorize")
	public String authorize() {
		var github = spiritProperties.getGithub();
		var serverAuthorizeUri = github.getAuthorizeUri()
				.concat("?")
				.concat("response_type=code")
				.concat("&client_id=").concat(github.getClientId())
				.concat("&redirect_uri=").concat(github.getRedirectUri())
				.concat("&scope=").concat(github.getScope())
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
		log.info("Github Resource Server invoke Client callback url, Authozation Code is {}", code);
		GithubProp github = spiritProperties.getGithub();
		String clientId = github.getClientId();
		String redirectUri = github.getRedirectUri();
		var checkMsg = githubService.checkCallbackParam(code, clientId, redirectUri);
		if(!StringUtils.isEmpty(checkMsg)) {
			return checkMsg;
		}
		AccessTokenReqVO accessTokenReq = new AccessTokenReqVO();
		accessTokenReq.setClientId(clientId);
		accessTokenReq.setClientSecret(github.getClientSecret());
		accessTokenReq.setCode(code);
		accessTokenReq.setRedirectUri(redirectUri);
		accessTokenReq.setState(state);
		try {
			githubService.applyAccessToken(accessTokenReq);
		} catch (Exception e) {
			log.error("获取AccessToken error", e);
			return e.getMessage();
		}
		
		return "redirect:/github/home";
	}
}
