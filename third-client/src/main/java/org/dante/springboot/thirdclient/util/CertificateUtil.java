package org.dante.springboot.thirdclient.util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/**
 * 证书工具类
 * 
 * @author dante
 *
 */
@Slf4j
public class CertificateUtil {
	
	/**
	 * 公钥进行加密，加密后 Base64 编码
	 * 
	 * @param encodePublicKey
	 * @param secret
	 * @return
	 */
	public static String encryption(String encodePublicKey, String secret) {
		return encryption(deserializationKey(encodePublicKey), secret);
	}
	
	public static String encryption(Key publicKey, String secret) {
		String encryptSecret = "";
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);	// 加密模式
			encryptSecret = Base64.getEncoder().encodeToString(cipher.doFinal(secret.getBytes()));
		} catch (Exception e) {
			log.error("公钥加密失败！", e);
		}
		return encryptSecret;
	}
	
	/**
	 * 反序列化编码后的密钥
	 * 
	 * @param encodeKey
	 * @return
	 */
	private static Key deserializationKey(String encodeKey) {
		Key key = null;
		try {
			@Cleanup ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(encodeKey));
			@Cleanup ObjectInputStream ois = new ObjectInputStream(bis);
			key = (Key) ois.readObject();
		} catch (Exception e) {
			log.error("反序列化错误！", e);
		}
		return key;
	}
	
	public static class KeyResult {
		private String publicKey;
		private String privateKey;
		
		public String getPublicKey() {
			return publicKey;
		}
		public void setPublicKey(String publicKey) {
			this.publicKey = publicKey;
		}
		public String getPrivateKey() {
			return privateKey;
		}
		public void setPrivateKey(String privateKey) {
			this.privateKey = privateKey;
		}
		
	}

}
