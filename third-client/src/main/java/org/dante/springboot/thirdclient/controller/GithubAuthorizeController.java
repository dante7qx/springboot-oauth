package org.dante.springboot.thirdclient.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.prop.GithubProp;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.service.JedisClient;
import org.dante.springboot.thirdclient.service.github.GithubAPIService;
import org.dante.springboot.thirdclient.service.github.GithubOAuthService;
import org.dante.springboot.thirdclient.vo.github.AccessTokenReqVO;
import org.dante.springboot.thirdclient.vo.github.GithubRepo;
import org.dante.springboot.thirdclient.vo.github.GithubUser;
import org.dante.springboot.thirdclient.vo.github.RepoReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/github")
public class GithubAuthorizeController {
	
	@Autowired
	private GithubOAuthService githubOAuthService;
	@Autowired
	private GithubAPIService githubAPIService;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private SpiritProperties spiritProperties;
	
	/**
	 * Github 主页
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/home")
	public String home(Model model) {
		try {
			String accessToken = jedisClient.getString(OAuthConsts.GITHUB_ACCESS_TOKEN);
			GithubUser user = githubAPIService.currentUser().block();
			model.addAttribute("accessToken", accessToken);
			model.addAttribute("user", user);
		} catch (Exception e) {
			log.error("findGithubResource error.", e);
			return "redirect:/";
		}
		return "github/index";
	}
	
	@GetMapping("/user")
	public @ResponseBody Mono<GithubUser> currentUser() {
		Mono<GithubUser> user = null;
		try {
			user = githubAPIService.currentUser();
		} catch (OAuthException e) {
			log.error("githubRepos error.", e);
		}
		return user;
	}
	
	@GetMapping("/repos")
	public @ResponseBody Flux<GithubRepo> githubRepos() {
		Flux<GithubRepo> repos = null;
		try {
			repos = githubAPIService.githubRepos(new RepoReqVO());
		} catch (OAuthException e) {
			log.error("githubRepos error.", e);
		}
		return repos;
	}
	
	/**
	 * 请求资源服务器的授权码 Code
	 * 
	 * @return
	 */
	@GetMapping("/oauth/authorize")
	public String authorize() {
		var accessToken = jedisClient.getString(OAuthConsts.GITHUB_ACCESS_TOKEN);
		var github = spiritProperties.getGithub();
		var serverAuthorizeUri = github.getAuthorizeUri()
				.concat("?")
				.concat("response_type=code")
				.concat("&client_id=").concat(github.getClientId())
				.concat("&redirect_uri=").concat(github.getRedirectUri())
				.concat("&scope=").concat(github.getScope())
				.concat("&state=").concat(RandomStringUtils.randomAlphabetic(6).toLowerCase());
		if(!StringUtils.isEmpty(accessToken)) {
			return "redirect:/github/home";
		}
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
		var checkMsg = githubOAuthService.checkCallbackParam(code);
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
			githubOAuthService.applyAccessToken(accessTokenReq);
		} catch (Exception e) {
			log.error("获取AccessToken error", e);
			return e.getMessage();
		}
		
		return "redirect:/github/home";
	}
}
