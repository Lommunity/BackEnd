package com.Lommunity.infrastructure.security;

import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    public Authentication authentication(String jwt) {
        try {
            String userId = jwtHelper.extractUserId(jwt);
            User user = userRepository.findWithRegionById(Long.parseLong(userId)).orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자는 존재하지 않습니다. userID: " + userId));
            // TODO: Check Register 추가하기
            return new JwtAuthenticationToken(user, jwt);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("jwt token has been expired.", e);
        } catch (Exception e) {
            throw new JwtAuthenticationException(e);
        }
    }
}
