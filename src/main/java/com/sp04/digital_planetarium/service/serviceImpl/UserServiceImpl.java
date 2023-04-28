package com.sp04.digital_planetarium.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.sp04.digital_planetarium.entity.User;
import com.sp04.digital_planetarium.repository.UserDao;
import com.sp04.digital_planetarium.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Nullable
    public User login(String username, String password) {
        User user = userDao.findByUsernameAndPassword(username, password);
        if (user != null) user.setPassword(null); // 防止密码泄露
        return user;
    }

    public User register(User user){
        if(userDao.existsByUsername(user.getUsername())){
            throw new ConstraintViolationException("用户名已存在", null);
        }
        user.setUid(0L);
        user = userDao.save(user);
        user.setPassword(null); // 防止密码泄露
        return user;
    }

    public User update(User user){
        User dbUser = userDao.findByUid(user.getUid());
        if (dbUser == null) throw new ConstraintViolationException("用户不存在", null);

        BeanUtil.copyProperties(user, dbUser, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        dbUser = userDao.save(dbUser);

        dbUser.setPassword(null); // 防止密码泄露
        return dbUser;
    }

    public User findByUid(Long uid){
        return userDao.findByUid(uid);
    }
}
