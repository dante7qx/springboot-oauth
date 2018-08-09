package org.dante.springboot.server.controller;

import org.dante.springboot.server.po.UserPO;
import org.dante.springboot.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RequestMapping("/user")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 获取所有用户
	 * 
	 * @return
	 */
	@GetMapping("/list")
	public Flux<UserPO> queryUsers() {
		return Flux.fromStream(userService.findUsers().stream());
	}
	
}
