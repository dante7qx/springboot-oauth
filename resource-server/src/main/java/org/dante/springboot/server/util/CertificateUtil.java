package org.dante.springboot.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
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
	 * 生成证书密钥对
	 * 
	 * @return
	 */
	public static KeyResult generateKeyPair() {
		KeyResult keyResult = null;
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(2048);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			keyResult = new KeyResult();
			keyResult.setPublicKey(serializeKey(keyPair.getPublic()));
			keyResult.setPrivateKey(serializeKey(keyPair.getPrivate()));
		} catch (NoSuchAlgorithmException e) {
			log.error("生成证书密钥对错误", e);
		}
		return keyResult;
	}
	
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
	 * 私钥进行解密
	 * 
	 * secret 公钥加密并Base64编码字符
	 * 
	 * @param encodePrivateKey
	 * @param secret 
	 * @return
	 */
	public static String decryption(String encodePrivateKey, String secret) {
		return decryption(deserializationKey(encodePrivateKey), secret);
	}
	
	public static String decryption(Key privateKey, String secret) {
		String decryptSecret = "";
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);	// 解密模式
			// Base64 解码 secret
			byte[] decodeSecretBytes = Base64.getDecoder().decode(secret);
			byte[] decryptBytes = cipher.doFinal(decodeSecretBytes);
			decryptSecret = new String(decryptBytes);
		} catch (Exception e) {
			log.error("私钥解密失败！", e);
		}
		return decryptSecret;
	}
	
	/**
	 * 序列化密钥 Key
	 * 
	 * @param key
	 * @return
	 */
	private static String serializeKey(Key key) {
		String encodeKey = null;
		try {
			@Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
			@Cleanup ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(key);
			encodeKey = Base64.getEncoder().encodeToString(bos.toByteArray());
		} catch (IOException e) {
			log.error("序列化错误！", e);
		}
		return encodeKey;
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
	
	public static void main(String[] args) {
		KeyResult keyPair = CertificateUtil.generateKeyPair();
		String publicKey = keyPair.getPublicKey();
		String privateKey = keyPair.getPrivateKey();
		System.out.println("编码后公钥：\n" + publicKey);
		System.out.println("编码后私钥：\n" + privateKey);
		Key key = CertificateUtil.deserializationKey(publicKey);
		System.out.println("反序列化 Key 的算法：" + key.getAlgorithm());
		String enSecret = CertificateUtil.encryption(publicKey, "12345");
		System.out.println("公钥加密：" + enSecret);
		String deSecret = CertificateUtil.decryption(privateKey, enSecret);
		System.out.println("私钥解密：" + deSecret);
	}

}
