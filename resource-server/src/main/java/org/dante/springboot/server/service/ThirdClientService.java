package org.dante.springboot.server.service;

import org.dante.springboot.server.dao.ThirdClientDAO;
import org.dante.springboot.server.po.ThirdClientPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdClientService {
	
	@Autowired
	private ThirdClientDAO thirdClientDAO;
	
	public ThirdClientPO findByClientId(String clientId) {
		return thirdClientDAO.findByClientId(clientId);
	}
	
}
