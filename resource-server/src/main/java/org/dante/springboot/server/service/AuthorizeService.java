package org.dante.springboot.server.service;

import java.util.UUID;

import org.dante.springboot.server.dao.ThirdClientDAO;
import org.dante.springboot.server.po.ThirdClientPO;
import org.dante.springboot.server.prop.SpiritProperties;
import org.dante.springboot.server.vo.AuthorizeTokenRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

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
	 * @throws JsonProcessingException 
	 */
	public String generateAuthCode(String clientId, String redirectUrl) throws JsonProcessingException {
		var thirdClient = thirdClientDAO.findByClientId(clientId);
		if(thirdClient == null || !redirectUrl.equals(thirdClient.getRedirectUri())) {
			return null;
		}
		var code = generateRandomStr();
		jedisClient.saveString(clientId, code, spiritProperties.getCodeExpireTime() * 60);
		return code;
	}
	
	/**
	 * 生成访问令牌 AccessToken
	 * 
	 * @param code
	 * @param clientId
	 * @param redirectUrl
	 * @return
	 */
	public AuthorizeTokenRespVO generateAccessToken(String code, String clientId, String redirectUrl) {
		var codeVal = jedisClient.getString(clientId);
		if (!code.equals(codeVal)) {
			return null;
		}
		var accessToken = generateRandomStr();
		var refreshToken = generateRandomStr();
		// 将 AccessToken、RefreshToken 保存到 Redis 中
		jedisClient.saveString(accessToken, clientId, spiritProperties.getExpireTime());
		jedisClient.saveNX(refreshToken, clientId);
		
		AuthorizeTokenRespVO resp = new AuthorizeTokenRespVO();
		resp.setAccessToken(accessToken);
		resp.setRefreshToken(refreshToken);
		resp.setExpiresIn(spiritProperties.getExpireTime());
		return resp;
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
