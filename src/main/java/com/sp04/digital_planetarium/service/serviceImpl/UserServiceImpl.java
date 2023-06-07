package com.sp04.digital_planetarium.service.serviceImpl;

import com.sp04.digital_planetarium.entity.User;
import com.sp04.digital_planetarium.repository.UserDao;
import com.sp04.digital_planetarium.service.UserService;
import com.sp04.digital_planetarium.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.validation.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public Optional<User> login(String username, String password) {
        Optional<User> user = Optional.ofNullable(userDao.findByUsernameAndPassword(username, password));
        user.ifPresent(u -> u.setPassword(null)); // 防止密码泄露
        return user;
    }

    public String getWebSocketToken(Long uid) {
        User user = findByUid(uid).orElseThrow(() -> new ConstraintViolationException("用户不存在", null));
        return JwtUtils.createJWT(user.getUid(), user.getUsername());
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
        if (user.getPassword() == null) user.setPassword(dbUser.getPassword());
        user = userDao.save(user);
        user.setPassword(null); // 防止密码泄露
        return user;
    }

    public Optional<User> findByUid(Long uid){
        return Optional.ofNullable(userDao.findByUid(uid));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userDao.findByUsername(username));
    }
}
