package org.dante.springboot.thirdclient;

import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.service.JedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ThirdClientApplicationTests {

	@Autowired
	private JedisClient jedisClient;
	
	@Test
	public void persistGithubAccessToken() {
		jedisClient.saveString(OAuthConsts.GITHUB_ACCESS_TOKEN, "df32b69247ece0244a88ca07b68635d80f26b9bf");
	}
	
}
