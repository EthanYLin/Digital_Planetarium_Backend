package com.sp04.digital_planetarium.service;

import com.sp04.digital_planetarium.entity.User;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;

public interface UserService {

    /**
     * 使用给定的用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return User(成功时, 密码会被置null) 或 null(参数为null或不存在时)
     */
    @Nullable
    User login(String username, String password);

    /**
     * 使用给定的User对象进行注册
     * @param user User对象(user.uid可以为任意值, 不影响注册)
     * @return User(uid为数据库中的值, 密码会被置null)
     * @throws ConstraintViolationException 如果用户名已存在或参数为null、空白字符串
     */
    User register(User user);

    /**
     * 更新用户信息
     * @param user User对象(user.uid必须存在), 不需要更新的字段保持null
     * @return 修改过后的User对象(密码会被置null)
     */
    User update(User user);

    /**
     * 根据uid查找用户
     * @param uid 用户uid
     * @return User对象(包括密码) 或 null(参数为null或不存在时)
     */
    User findByUid(Long uid);
}
