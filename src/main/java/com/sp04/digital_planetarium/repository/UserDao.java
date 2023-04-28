package com.sp04.digital_planetarium.repository;

import com.sp04.digital_planetarium.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByUid(Long uid);

    User findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    boolean existsByUid(Long uid);

}
