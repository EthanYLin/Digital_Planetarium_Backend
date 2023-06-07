package com.sp04.digital_planetarium.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;


public class JwtUtils {
    private static final String SECRET_KEY = "DigitalPlanetarium"; // JWT加密密钥
    private static final long EXPIRATION_TIME = 60000; // JWT过期时间，单位为毫秒(1分钟)

    /**
     * 生成JWT
     *
     * @param uid 用户ID
     * @param username 用户名
     * @return JWT
     */
    public static String createJWT(Long uid, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(uid.toString())
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析JWT
     *
     * @param jwt JWT
     * @return Claims对象
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
