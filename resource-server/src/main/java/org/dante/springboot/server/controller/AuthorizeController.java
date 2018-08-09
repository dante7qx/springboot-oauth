package org.dante.springboot.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.dante.springboot.server.service.AuthorizeService;
import org.dante.springboot.server.vo.AuthorizeCodeVO;
import org.dante.springboot.server.vo.AuthorizeTokenReqVO;
import org.dante.springboot.server.vo.AuthorizeTokenRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		/**
		 * 重定向（redirect:/oauth/login）时使用 RedirectAttributes 传递参数 
		 */
		/*
		String responseType = request.getParameter("response_type");
		String clientId = request.getParameter("client_id");
		String redirectUri = request.getParameter("redirect_uri");
		String scope = request.getParameter("scope");
		String state = request.getParameter("state");
		redirectAttributes.addFlashAttribute("response_type", responseType);
		redirectAttributes.addFlashAttribute("client_id", clientId);
		redirectAttributes.addFlashAttribute("redirect_uri", redirectUri);
		redirectAttributes.addFlashAttribute("scope", scope);
		redirectAttributes.addFlashAttribute("state", state);
		return "redirect:/oauth/login";
		*/
		return "forward:/oauth/login";
	}
	
	/**
	 * 获取授权码 Code
	 * 
	 * @param authorizeCodeVO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/auth/code")
	public String authCode(AuthorizeCodeVO authorizeCodeVO) {
		log.info("请求获取授权码 Code ==> {}", authorizeCodeVO);
		String clientId = authorizeCodeVO.getClientId();
		String redirectUri = authorizeCodeVO.getRedirectUri();
		String scope = authorizeCodeVO.getScope();
		String state = authorizeCodeVO.getState();
		String code = "";
		try {
			code = authorizeService.generateAuthCode(authorizeCodeVO);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "redirect:".concat(redirectUri)
				.concat("?")
				.concat("grant_type=authorization_code")
				.concat("&code=" + code)
				.concat("&redirect_uri=" + redirectUri)
				.concat("&client_id=" + clientId)
				.concat("&scope=" + scope)
				.concat("&state=" + state);
	}
	
	/**
	 * 获取访问令牌 AccessToken
	 * 
	 * @param authorizeCodeVO
	 * @return
	 */
	@PostMapping("/auth/token")
	@ResponseBody
	public ResponseEntity<AuthorizeTokenRespVO> authToken(@RequestBody AuthorizeTokenReqVO authorizeTokenVO) {
		ResponseEntity<AuthorizeTokenRespVO> resp = null;
		try {
			AuthorizeTokenRespVO accessToken = authorizeService.generateAccessToken(authorizeTokenVO);
			resp = ResponseEntity.ok(accessToken);
		} catch (Exception e) {
			log.error(e.getMessage());
			resp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("ErrorMsg", e.getMessage())
					.build();
		}
		return resp;
	}
	

}
