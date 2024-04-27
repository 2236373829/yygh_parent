package com.atguigu.yygh.common.helper;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author xyzZero3
 * @create 2024-04-22 17:26
 */
public class JwtHelper {

    // token过期时间
    final private static long tokenExpiration = 24 * 60 * 60 * 1000;

    // 签名密钥
    final private static String tokenSignKey = "xyzZero3";

    /**
     * 根据参数生成token
     *
     * @param userId
     * @param userName
     * @return
     */
    public static String createToken(Long userId, String userName) {
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder.setSubject("YYGH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) return null;

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");

        return userId.longValue();
    }

    public static String getUserName(String token) {
        if (StringUtils.isEmpty(token)) return "";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        return (String) claims.get("userName");
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken(1123123123L, "郭华波");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUserName(token));
    }

}
