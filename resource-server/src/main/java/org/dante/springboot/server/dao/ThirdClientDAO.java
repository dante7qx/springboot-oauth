package org.dante.springboot.server.dao;

import org.dante.springboot.server.po.ThirdClientPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdClientDAO extends JpaRepository<ThirdClientPO, Long>{
	public ThirdClientPO findByClientId(String clientId);
}
