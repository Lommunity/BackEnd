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
            User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자는 존재하지 않습니다. userID: " + userId));
            // 실제 인증을 처리한다. jwt에서 userId를 추출하고, userRepository에서 userId에 해당하는 user 데이터가 있는지 찾는다. (인증 완료)
            return new JwtAuthenticationToken(user, jwt);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("jwt token has been expired.", e);
        } catch (Exception e) {
            throw new JwtAuthenticationException(e);
        }
    }
}
