package org.dante.springboot.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.dante.springboot.server.service.AuthorizeService;
import org.dante.springboot.server.vo.AuthorizeCodeVO;
import org.dante.springboot.server.vo.AuthorizeTokenReqVO;
import org.dante.springboot.server.vo.AuthorizeTokenRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/oauth")
public class AuthorizeController {
	
	@Autowired
	private AuthorizeService authorizeService;
	
	@RequestMapping("/authorize")
	public String applyAuthorizeCode(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String responseType = request.getParameter("response_type");
		String clientId = request.getParameter("client_id");
		String redirectUri = request.getParameter("redirect_uri");
		String scope = request.getParameter("scope");
		String state = request.getParameter("state");

		redirectAttributes.addFlashAttribute("responseType", responseType);
		redirectAttributes.addFlashAttribute("clientId", clientId);
		redirectAttributes.addFlashAttribute("redirectUri", redirectUri);
		redirectAttributes.addFlashAttribute("scope", scope);
		redirectAttributes.addFlashAttribute("state", state);
		return "redirect:/oauth/login";
	}
	
	/**
	 * 获取授权码 Code
	 * 
	 * @param authorizeCodeVO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/auth/code")
	public String authCode(AuthorizeCodeVO authorizeCodeVO) throws Exception {
		log.info("AuthorizeCodeVO ==> " + authorizeCodeVO);
		String clientId = authorizeCodeVO.getClientId();
		String redirectUri = authorizeCodeVO.getRedirectUri();
		String scope = authorizeCodeVO.getScope();
		String code = authorizeService.generateAuthCode(clientId, redirectUri);
		if(code == null) {
			throw new Exception("ClientId [" + clientId + "] 无效！");
		}
		return "redirect:".concat(redirectUri)
				.concat("?")
				.concat("grant_type=authorization_code")
				.concat("&code=" + code)
				.concat("&redirect_uri=" + redirectUri)
				.concat("&client_id=" + clientId)
				.concat("&scope=" + scope);
	}
	
	/**
	 * 获取访问令牌 AccessToken
	 * 
	 * @param authorizeCodeVO
	 * @return
	 */
	@PostMapping("/auth/token")
	@ResponseBody
	public AuthorizeTokenRespVO authToken(@RequestBody AuthorizeTokenReqVO authorizeTokenVO) {
		String code = authorizeTokenVO.getCode();
		String clientId = authorizeTokenVO.getClientId();
		String redirectUri = authorizeTokenVO.getRedirectUri();
		AuthorizeTokenRespVO accessToken = authorizeService.generateAccessToken(code, clientId, redirectUri);
		return accessToken;
	}
	

}
