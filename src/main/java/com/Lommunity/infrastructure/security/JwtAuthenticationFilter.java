package com.Lommunity.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = resolveJwt(request);
            if (jwt != null) {
                Authentication authentication = jwtAuthenticationProvider.authentication(jwt);
                // authentication 객체를 SecurityContextHolder에 넣는 과정
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            // 인증 실패
            SecurityContextHolder.clearContext();
            authenticationExceptionHandler.handleException(response, e);
        }
    }

    public String resolveJwt(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                       .filter(authorization -> authorization.startsWith("Bearer "))
                       .map(authorization -> authorization.substring("Bearer ".length()))
                       .orElse(null);
    }
}
