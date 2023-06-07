package com.sp04.digital_planetarium.controller;

import com.sp04.digital_planetarium.entity.Response;
import com.sp04.digital_planetarium.entity.User;
import com.sp04.digital_planetarium.exception.BadRequestException;
import com.sp04.digital_planetarium.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> registerRequest,
                                      HttpServletRequest request) throws BadRequestException {

        if (request.getSession().getAttribute("uid") != null) {
            throw new BadRequestException("已登录", null);
        }

        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        Optional<User> user = userService.login(username, password);

        if (user.isEmpty()) {
            throw new BadRequestException("用户名或密码错误", null);
        } else {
            request.getSession().setAttribute("uid", user.get().getUid());
            return ResponseEntity.ok(user.get());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("uid");
        Response response = new Response(200, "登出成功");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest,
                                      HttpServletRequest request) throws BadRequestException {
        if (request.getSession().getAttribute("uid") != null) {
            throw new BadRequestException("已登录", null);
        }
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        User user = new User(username, password); //采用自增uid与随机形象
        user = userService.register(user);
        request.getSession().setAttribute("uid", user.getUid());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user, HttpServletRequest request)
            throws BadRequestException {
        Long uid = (Long) request.getSession().getAttribute("uid");
        if (uid == null) {
            throw new BadRequestException("未登录", null);
        } else if(!uid.equals(user.getUid())){
            throw new BadRequestException("uid不匹配", null);
        }
        user = userService.update(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/webSocketToken")
    public ResponseEntity<?> getWebSocketToken(HttpServletRequest request) throws BadRequestException {
        Long uid = (Long) request.getSession().getAttribute("uid");
        if (uid == null) {
            throw new BadRequestException("未登录", null);
        }
        String token = userService.getWebSocketToken(uid);
        Map<String, String> map = Map.of("token", token);
        return ResponseEntity.ok(map);
    }

}
