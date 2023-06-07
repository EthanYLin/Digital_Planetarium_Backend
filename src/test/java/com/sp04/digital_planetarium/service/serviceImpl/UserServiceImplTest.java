package com.sp04.digital_planetarium.service.serviceImpl;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.User;
import com.sp04.digital_planetarium.repository.UserDao;
import com.sp04.digital_planetarium.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
//        User user = new User();
//        user.setUid(1L);
//        user.setUsername("testUsername");
//        user.setPassword("testPassword");
//        userDao.save(user);
    }

    @Test
    public void testJWT(){
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJhZG1pbiIsImlhdCI6MTY4NjEyNDIxNiwiZXhwIjoxNjg2MTI0Mjc2fQ.2Xk_5ggA31Mhq236Gj8_k0SkwakVD1ioF_RY7r-xp4Y";
        Claims claims = JwtUtils.parseJWT(jwt);
        System.out.println(claims.getSubject());
        System.out.println(claims.get("username"));

    }

    //使用正确用户名和密码登录
    @Test
    public void loginServiceCorrectly() {
//        String username = "testUsername";
//        String password = "testPassword";
//        User user = userServiceImpl.login(username, password);
//        assertNotNull(user);
//        assertEquals(username, user.getUsername());
//        assertEquals("", user.getPassword());
    }

    //使用错误用户名、密码、null登录
    @Test
    public void loginServiceWithNull() {
//        User[] users = new User[4];
//        users[0] = userServiceImpl.login(null, null);
//        users[1] = userServiceImpl.login("testUsername", null);
//        users[2] = userServiceImpl.login("wrongUsername", "testPassword");
//        users[3] = userServiceImpl.login("testUsername", "wrongPassword");
//        for (User user : users) {
//            assertNull(user);
//        }
    }

    static List<Arguments> registerService_param(){
        int OK = 1;
        int CONSTRAINT_VIOLATION_EXCEPTION = 2;
        return List.of(
                // 参数化测试格式: 用户ID, 用户名, 密码, 预期结果
                // 用户ID: 库中不存在的、默认值、存在的
                Arguments.of(2L, "user2", "pwd", OK),
                Arguments.of(0L, "user3", "pwd", OK),
                Arguments.of(1L, "user4", "pwd", OK),
                // 用户名: null, 空格字符串, 已存在的用户名
                Arguments.of(0L, null, "pwd", CONSTRAINT_VIOLATION_EXCEPTION),
                Arguments.of(0L, "  ", "pwd", CONSTRAINT_VIOLATION_EXCEPTION),
                Arguments.of(0L, "testUsername", "pwd", CONSTRAINT_VIOLATION_EXCEPTION),
                // 密码: null, 空格字符串
                Arguments.of(0L, "user5", null, CONSTRAINT_VIOLATION_EXCEPTION),
                Arguments.of(0L, "user6", "  ", CONSTRAINT_VIOLATION_EXCEPTION)
        );
    }

    @ParameterizedTest
    @MethodSource("registerService_param")
    public void registerService(long uid, String username, String password, int expected){
        int OK = 1;
        int CONSTRAINT_VIOLATION_EXCEPTION = 2;
        User user = new User(uid, username, password, new Figure(true));
        if(expected == OK){
            User result = userServiceImpl.register(user);
            assertEquals(user.getUsername(), result.getUsername());
            assertEquals(user.getPassword(), result.getPassword());
            assertNotEquals(0, result.getUid());
        }
        else if(expected == CONSTRAINT_VIOLATION_EXCEPTION){
            assertThrows(ConstraintViolationException.class, () -> {
                userServiceImpl.register(user);
            });
        }
        else fail();
    }

}
