package com.Lommunity.application.login;

import com.Lommunity.application.login.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestOauthLoginServiceTest {
    @Autowired
    TestOauthLoginService testOauthLoginService;

    @Test
    public void testLoginTest() {
        String randomId = UUID.randomUUID().toString();
        LoginRequest loginRequest = new LoginRequest(randomId, "test");
        Oauth2UserInfo testerInfo = testOauthLoginService.login(loginRequest);
        assertThat(testerInfo.getProviderId()).isEqualTo(randomId);
        assertThat(testerInfo.getName()).isEqualTo("테스트 유저 " + randomId);
    }
}