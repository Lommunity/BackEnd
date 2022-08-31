package com.Lommunity.infrastructure.security;

import com.Lommunity.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Getter
@Builder
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final User user;
    private final String jwt;

    public JwtAuthenticationToken(User user, String jwt) {
        // 객체 생성 시 인가 정보(Authorization)은 AbstractAuthenticationToken에 넘겨준다.
        super(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
        this.user = user;
        this.jwt = jwt;
    }


    @Override
    public Object getPrincipal() {
        return user;
    } // 아이디

    @Override
    public Object getCredentials() {
        return jwt;
    } // 비밀번호

    @Override
    public boolean isAuthenticated() {
        return user != null;
    }
}
