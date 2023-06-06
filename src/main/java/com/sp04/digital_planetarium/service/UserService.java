package com.sp04.digital_planetarium.service;

import com.sp04.digital_planetarium.entity.User;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;

import java.util.Optional;

public interface UserService {

    /**
     * 使用给定的用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return Optional:User(密码会被置空)
     */
    Optional<User> login(String username, String password);

    /**
     * 使用给定的User对象进行注册
     * @param user User对象(user.uid可以为任意值, 不影响注册)
     * @return User(uid为数据库中的值, 密码会被置null)
     * @throws ConstraintViolationException 如果用户名已存在或参数为null、空白字符串
     */
    User register(User user);

    /**
     * 更新用户信息
     * @param user User对象(user.uid必须存在), 若密码不需要更新则可以设置为null
     * @return 修改过后的User对象(密码会被置null)
     * @throws ConstraintViolationException user.uid不存在
     */
    User update(User user);

    /**
     * 根据uid查找用户
     * @param uid 用户uid
     * @return Optional:User(包括密码)
     */
    Optional<User> findByUid(Long uid);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return Optional:User(密码会被置null)
     */
    Optional<User> findByUsername(String username);
}
