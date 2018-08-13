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
	
	@Test
	public void persistFacebookToken() {
		jedisClient.saveString(OAuthConsts.FACEBOOK_ACCESS_TOKEN, "EAAMDzHgGGYEBAPeJKSrQ1vQXOmUbRUd5db3ArZCzipvK0UL42vKEnTdQ96VI943khHZBe9CHmQFuz3qFznGJGWZA8kApR9znuRIwSFZCfTyQKaAqjQPSUXWpT5PNI4n9gdKTYZCHaayikyCgvjmkGGmQ5FQyibNR92aZCbCIDS6HLaZBWBpxo9q", 5183803);
	}
	
	
	
}
