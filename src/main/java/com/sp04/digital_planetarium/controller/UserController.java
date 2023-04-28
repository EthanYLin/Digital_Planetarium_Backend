package com.sp04.digital_planetarium.controller;

import com.sp04.digital_planetarium.entity.User;
import com.sp04.digital_planetarium.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    //TODO: maybe should change the implement of session
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password,
                                      HttpServletRequest request) {
        User user = userService.login(username, password);
        //TODO: 防止重复登录
        if (user == null) {
            return ResponseEntity.badRequest().body("用户名或密码错误");
        } else {
            request.getSession().setAttribute("uid", user.getUid());
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("uid");
        return ResponseEntity.ok("登出成功");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password,
                                      HttpServletRequest request) {
        //TODO: 防止登录后再注册
        //TODO: 处理用户名已存在的错误
        User user = new User(username, password); //采用自增uid与随机形象
        user = userService.register(user);
        request.getSession().setAttribute("uid", user.getUid());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user, HttpServletRequest request) {
        Long uid = (Long) request.getSession().getAttribute("uid");
        if (uid == null) {
            return ResponseEntity.badRequest().body("未登录");
        }
        user.setUid(uid);
        user = userService.update(user);
        return ResponseEntity.ok(user);
    }

}
