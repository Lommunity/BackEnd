package com.Lommunity.infrastructure.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
