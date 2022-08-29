package com.Lommunity.application.login;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequest {

    private String accessToken;
    private String provider;
}
