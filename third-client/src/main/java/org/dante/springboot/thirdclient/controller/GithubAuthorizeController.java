package org.dante.springboot.thirdclient.controller;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.prop.SpiritProperties;
import org.dante.springboot.thirdclient.vo.dante.DanteUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/github")
public class GithubAuthorizeController {
	
	@Autowired
	private SpiritProperties spiritProperties;
	
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
	@ResponseBody
	public String home() {
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
}
