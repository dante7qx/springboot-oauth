package org.dante.springboot.server;

import org.dante.springboot.server.dao.ThirdClientDAO;
import org.dante.springboot.server.po.ThirdClientPO;
import org.dante.springboot.server.service.AuthorizeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServerApplicationTests {

	@Autowired
	private ThirdClientDAO thirdClientDAO;
	@Autowired
	private AuthorizeService authorizeService;
	
	@Test
	public void findByClientId() {
		ThirdClientPO thirdClient = thirdClientDAO.findByClientId("6DE66752F7FC4F1AA4AD6E792D12A45E");
		log.info("ThirdClientPO => {}", thirdClient);
	}
	
	@Test
	public void generateAuthCode() {
		String authCode = null;
		try {
			authCode = authorizeService.generateAuthCode("1122", "http://localhost:8001/auth/callback");
		} catch (JsonProcessingException e) {
			log.error("generateAuthCode error.", e);
		}
		log.info("authCode => {}", authCode);
	}
	
}
