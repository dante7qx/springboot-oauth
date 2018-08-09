package org.dante.springboot.server;

import org.dante.springboot.server.dao.ThirdClientDAO;
import org.dante.springboot.server.po.ThirdClientPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServerApplicationTests {

	@Autowired
	private ThirdClientDAO thirdClientDAO;

	@Test
	public void findByClientId() {
		ThirdClientPO thirdClient = thirdClientDAO.findByClientId("6de66752f7fc4f1aa4ad6e792d12a45e");
		log.info("ThirdClientPO => {}", thirdClient);
	}

}
