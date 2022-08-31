package com.Lommunity.application.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    // 로그인 완료시 응답
    private String jwt;
    private UserLoginDto user;
}
