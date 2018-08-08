package org.dante.springboot.server.dao;

import org.dante.springboot.server.po.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<UserPO, Long> {

}
