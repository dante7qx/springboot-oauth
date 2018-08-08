package org.dante.springboot.thirdclient.exception;

public class OAuthException extends Exception {

	private static final long serialVersionUID = -3180530951608050408L;

	/**
	 * 构造函数
	 */
	public OAuthException() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public OAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 */
	public OAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 */
	public OAuthException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 */
	public OAuthException(Throwable cause) {
		super(cause);
	}

}
