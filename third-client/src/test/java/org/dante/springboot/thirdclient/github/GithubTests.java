package org.dante.springboot.thirdclient.github;

import org.dante.springboot.thirdclient.ThirdClientApplicationTests;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.service.github.GithubAPIService;
import org.dante.springboot.thirdclient.service.github.GithubOAuthService;
import org.dante.springboot.thirdclient.vo.github.AccessTokenReqVO;
import org.dante.springboot.thirdclient.vo.github.GithubRepo;
import org.dante.springboot.thirdclient.vo.github.RepoReqVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class GithubTests extends ThirdClientApplicationTests {
	@Autowired
	private GithubOAuthService githubOAuthService;
	@Autowired
	private GithubAPIService githubAPIService;
	
	@Test
	public void applyAccessToken() {
		AccessTokenReqVO req = new AccessTokenReqVO();
		req.setClientId("2a072c55d48f194676f7");
		req.setClientSecret("0b37929df2bb3689f8605e11f2c4306f649cebb9");
		req.setRedirectUri("http://spiritprd-oauth-client.test.bizjetcloud.com/github/oauth/callback");
		req.setCode("7d075ceb4a94af111313");
		req.setState("tkakxx");
		log.info("Req token {}", req);
		
		try {
			githubOAuthService.applyAccessToken(req);
		} catch (Exception e) {
			log.error("applyAccessToken error.", e);
		}
	}
	
	@Test
	public void currentUser() {
		try {
			githubAPIService.currentUser().subscribe(currentUser -> log.info("GithubUser {}", currentUser));
		} catch (OAuthException e) {
			log.error("获取Github CurrentUser error.", e);
		}
	}
	
	@Test
	public void githubRepos() {
		try {
			Flux<GithubRepo> githubRepos = githubAPIService.githubRepos(new RepoReqVO());
			log.info("Github repos {}", githubRepos.count().block());
		} catch (OAuthException e) {
			log.error("获取Github repos error.", e);
		}
	}
}
