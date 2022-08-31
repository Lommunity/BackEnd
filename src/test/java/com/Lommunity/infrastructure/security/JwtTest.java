package com.Lommunity.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class JwtTest {
    public static void main(String[] args) {
        // jwt 생성
        String userId = "12";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        // jwt를 암호화하고, 복호화 하는데 대칭키 방식을 사용한다.
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String jwt = Jwts.builder()
                         .setSubject(userId)
                         .setIssuedAt(Date.from(now.toInstant()))
                         .setExpiration(Date.from(now.plusMinutes(60).toInstant()))
                         .signWith(key)
                         .compact();

        System.out.println(jwt);

        // 프론트에게서 jwt를 전달받았다고 가정하고, userId를 파싱
        String userIdFromJwt = Jwts.parserBuilder()
                                   .setSigningKey(key)
                                   .build()
                                   .parseClaimsJws(jwt)
                                   .getBody()
                                   .getSubject();
        System.out.println("userId from jwt: " + userIdFromJwt);
    }
}
