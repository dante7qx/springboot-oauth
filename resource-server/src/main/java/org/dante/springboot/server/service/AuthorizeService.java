package org.dante.springboot.server.service;

import java.util.Arrays;
import java.util.UUID;

import org.dante.springboot.server.dao.ThirdClientDAO;
import org.dante.springboot.server.po.ThirdClientPO;
import org.dante.springboot.server.prop.SpiritProperties;
import org.dante.springboot.server.util.CertificateUtil;
import org.dante.springboot.server.vo.AuthorizeCodeVO;
import org.dante.springboot.server.vo.AuthorizeTokenReqVO;
import org.dante.springboot.server.vo.AuthorizeTokenRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly=true)
public class AuthorizeService {

	@Autowired
	private ThirdClientDAO thirdClientDAO;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private SpiritProperties spiritProperties;
	
	/**
	 * 生成授权码 Code
	 * 
	 * @param clientId
	 * @param redirectUrl
	 * @return
	 * @throws Exception 
	 */
	public String generateAuthCode(AuthorizeCodeVO authorizeCodeReq) throws Exception {
		var errorMsg = checkReqAuthCodeParam(authorizeCodeReq);
		if(!StringUtils.isEmpty(errorMsg)) {
			throw new Exception("生成授权码 Code 错误，错误信息：" + errorMsg);
		}
		var code = generateRandomStr();
		jedisClient.saveString(authorizeCodeReq.getClientId(), code, spiritProperties.getCodeExpireTime() * 60);
		return code;
	}
	
	/**
	 * 生成访问令牌 AccessToken
	 * 
	 * 将 AccessToken、RefreshToken 保存到 Redis 中
	 * 
	 * @param code
	 * @param clientId
	 * @param redirectUrl
	 * @return
	 * @throws Exception 
	 */
	public AuthorizeTokenRespVO generateAccessToken(AuthorizeTokenReqVO tokenReq) throws Exception {
		String errorMsg = checkReqTokenParam(tokenReq);
		if(!StringUtils.isEmpty(errorMsg)) {
			throw new Exception("生成访问令牌 AccessToken 错误，错误信息：" + errorMsg);
		}
		var clientId = tokenReq.getClientId();
		var accessToken = generateRandomStr();
		var refreshToken = generateRandomStr();
		jedisClient.saveString(accessToken, clientId, spiritProperties.getExpireTime());
		jedisClient.saveNX(refreshToken, clientId);
		AuthorizeTokenRespVO resp = new AuthorizeTokenRespVO();
		resp.setAccessToken(accessToken);
		resp.setRefreshToken(refreshToken);
		resp.setExpiresIn(spiritProperties.getExpireTime());
		return resp;
	}
	
	/**
	 * 检测请求 AuthCode 参数的有效性
	 * 
	 * @param authCodeReq
	 * @return
	 */
	private String checkReqAuthCodeParam(AuthorizeCodeVO authCodeReq) {
		var builder = new StringBuilder();
		var thirdClient = thirdClientDAO.findByClientId(authCodeReq.getClientId());
		if(thirdClient == null) {
			builder.append("ClientId没有注册信息！");
		} else if(!checkScopeValid(thirdClient.getScope(), authCodeReq.getScope())) {
			builder.append("ClientId没有任何资源的权限！");
		}
		return builder.toString();
	}
	
	/**
	 * 检测请求 AccessToken 参数的有效性
	 * 
	 * @param tokenReq
	 * @return
	 */
	private String checkReqTokenParam(AuthorizeTokenReqVO tokenReq) {
		var builder = new StringBuilder();
		var clientId = tokenReq.getClientId();
		var clientSecret = tokenReq.getClientSecret();
		var code = tokenReq.getCode();
		var thirdClient = thirdClientDAO.findByClientId(clientId);
		if(thirdClient == null) {
			builder.append("ClientId没有注册信息！");
		} else {
			var codeInRedis = jedisClient.getString(clientId);
			if(!StringUtils.pathEquals(code, codeInRedis)) {
				builder.append("授权码code错误！");
			}
			String sourceClientSecret = CertificateUtil.decryption(thirdClient.getPrivateKey(), clientSecret);
			if(!StringUtils.pathEquals(thirdClient.getClientSecret(), sourceClientSecret)) {
				builder.append("ClientSecret错误！");
			}
		}
		return builder.toString();
	}
	
	/**
	 * 检测 AccessToken 的有效性
	 * 
	 * @param accessToken
	 * @return
	 */
	public boolean checkAccessToken(String accessToken) {
		if(StringUtils.isEmpty(accessToken)) {
			return false;
		}
		String clientId = jedisClient.getString(accessToken);
		if(StringUtils.isEmpty(clientId)) {
			return false;
		}
		ThirdClientPO thirdClient = thirdClientDAO.findByClientId(clientId);
		if(thirdClient == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 验证访问权限有效性
	 * 
	 * @param serverScope
	 * @param reqScope
	 * @return
	 */
	private boolean checkScopeValid(String serverScope, String reqScope) {
		var serverArr = serverScope.split(",");
		var reqArr = reqScope.split(",");
		if(reqArr.length == 0) {
			return false;
		}
		var valid = false;
		var serverScopes = Arrays.asList(serverArr);
		for (String r : reqArr) {
			if(serverScopes.contains(r)) {
				valid = true;
				break;
			}
		}
		return valid;
	}
	
	/**
	 * 生成随机字符串
	 * 
	 * @return
	 */
	private String generateRandomStr() {
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	}
	
	public static enum AUTH_TYPE {
		CODE,
		TOKEN
	}
	
}
