package com.Lommunity.application.login;

import com.Lommunity.application.login.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestOauthLoginService {

    public Oauth2UserInfo login(LoginRequest loginRequest) {
        return Oauth2UserInfo.builder()
                             .providerId(loginRequest.getAccessToken())
                             .name("테스트 유저 " + loginRequest.getAccessToken())
                             .build();
    }
}
