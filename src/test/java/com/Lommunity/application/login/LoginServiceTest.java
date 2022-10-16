package com.Lommunity.application.login;

import com.Lommunity.application.login.dto.LoginRequest;
import com.Lommunity.application.login.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Test
    public void testLoginTest() {
        String randomId = UUID.randomUUID().toString();
        LoginRequest loginRequest = new LoginRequest(randomId, "test");
        LoginResponse loginResponse = loginService.login(loginRequest);
        assertThat(loginResponse.getUser().getNickname()).isEqualTo("테스트 유저 " + randomId);
    }
}