package com.Lommunity.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTest {

    @Test
    public void jwtTest() {

        // given
        String userId = "12";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String jwt = Jwts.builder()
                         .setSubject(userId)
                         .setIssuedAt(Date.from(now.toInstant()))
                         .setExpiration(Date.from(now.plusMinutes(60).toInstant()))
                         .signWith(key)
                         .compact();

        // when
        String userIdFromJwt = Jwts.parserBuilder()
                                   .setSigningKey(key)
                                   .build()
                                   .parseClaimsJws(jwt)
                                   .getBody()
                                   .getSubject();

        // then
        assertThat(userIdFromJwt).isEqualTo(userId);
    }

    @Test
    public void jwtEncodingAndDecodingTest() {

        // given
        SecretKey originalKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encodedKey = Encoders.BASE64URL.encode(originalKey.getEncoded());
        System.out.println(encodedKey);

        // when
        SecretKey decodedKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(encodedKey));

        // then
        assertThat(originalKey).isEqualTo(decodedKey);

    }
}
