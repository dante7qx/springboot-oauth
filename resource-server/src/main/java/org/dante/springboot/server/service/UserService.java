package org.dante.springboot.server.service;

import java.util.List;

import org.dante.springboot.server.dao.UserDAO;
import org.dante.springboot.server.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * 获取所有的用户
	 * 
	 * @return
	 */
	public List<UserPO> findUsers() {
		return userDAO.findAll();
	}
	
}
