package com.Lommunity.infrastructure.security;

import com.Lommunity.domain.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class JwtHelper {

    private final static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final static Integer EXPIRY_MINUITES = 60;

    public String createJwt(User user) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return Jwts.builder()
                   .setSubject(user.getId().toString())
                   .setIssuedAt(Date.from(now.toInstant()))
                   .setExpiration(Date.from(now.plusMinutes(EXPIRY_MINUITES).toInstant()))
                   .signWith(SECRET_KEY)
                   .compact();
    }

    public String extractUserId(String jwt) {
        // jwt에서 userId 꺼내는 메서드
        return Jwts.parserBuilder()
                   .setSigningKey(SECRET_KEY)
                   .build()
                   .parseClaimsJws(jwt)
                   .getBody()
                   .getSubject(); // subject에 userId를 넣어서 jwt를 만들었었다.
    }
}
