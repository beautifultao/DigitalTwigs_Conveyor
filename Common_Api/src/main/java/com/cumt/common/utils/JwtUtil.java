package com.cumt.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String,Object> claims) {
        // 生成签名密钥
        SecretKey signingKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));

        // 令牌有效时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + ttlMillis;
        Date exp = new Date(expMillis);



        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(signingKey);
        return builder.compact();
    }

    /**
     * Token解密
     * @param secretKey jwt秘钥
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJwt(String secretKey, String token){
        SecretKey signingKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
