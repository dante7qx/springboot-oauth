package org.dante.springboot.thirdclient.service.github;

import org.dante.springboot.thirdclient.constant.OAuthConsts;
import org.dante.springboot.thirdclient.exception.OAuthException;
import org.dante.springboot.thirdclient.vo.github.GithubRepo;
import org.dante.springboot.thirdclient.vo.github.GithubUser;
import org.dante.springboot.thirdclient.vo.github.RepoReqVO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubAPIService extends GithubAbstractService {
	
	/**
	 * 当前 Github 用户详情
	 * 
	 * @return
	 * @throws OAuthException 
	 */
	public Mono<GithubUser> currentUser() throws OAuthException {
		return apiWebClient.get()
				.uri("/user")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.header(OAuthConsts.HEADER_KEY, buildReqHeader())
				.retrieve()
				.bodyToMono(GithubUser.class);
	}
	
	/**
	 * 获取当前用户 Github Repo
	 * 
	 * @param repoReq
	 * @return
	 * @throws OAuthException 
	 */
	public Flux<GithubRepo> githubRepos(RepoReqVO repoReq) throws OAuthException {
		return apiWebClient.get()
				.uri("/user/repos?page={page}&per_page={perPage}", repoReq.param())
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.header(OAuthConsts.HEADER_KEY, buildReqHeader())
				.retrieve()
				.bodyToFlux(GithubRepo.class);
	}
}
