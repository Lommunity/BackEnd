package com.Lommunity.controller;

import com.Lommunity.application.login.LoginService;
import com.Lommunity.application.login.dto.LoginRequest;
import com.Lommunity.application.login.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginService.login(request);
    }
}
