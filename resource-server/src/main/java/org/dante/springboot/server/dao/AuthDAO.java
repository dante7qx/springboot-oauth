package org.dante.springboot.server.dao;

import org.dante.springboot.server.po.AuthPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthDAO extends JpaRepository<AuthPO, Long> {
	
	/**
	 * 根据 Code 和 AccessToken 获取
	 * 
	 * @param authVal
	 * @return
	 */
	public AuthPO findByAuthVal(String authVal);
	
}
