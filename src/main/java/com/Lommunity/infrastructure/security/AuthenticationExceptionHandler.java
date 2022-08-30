package com.Lommunity.infrastructure.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        handleException(response, authException);
    }

    public void handleException(HttpServletResponse response, AuthenticationException exception) {
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized. " + exception.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
