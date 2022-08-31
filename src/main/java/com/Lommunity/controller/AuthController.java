package com.Lommunity.controller;

import com.Lommunity.application.login.LoginService;
import com.Lommunity.application.login.dto.LoginRequest;
import com.Lommunity.application.login.dto.LoginResponse;
import com.Lommunity.application.user.UserDto;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.JoinRequest;
import com.Lommunity.application.user.dto.JoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginService.login(request);
    }
}
