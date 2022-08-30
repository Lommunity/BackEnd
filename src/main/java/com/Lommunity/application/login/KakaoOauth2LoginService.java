package com.Lommunity.application.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOauth2LoginService {

    private final RestTemplate restTemplate;

    @Getter
    private static class KakaoProfileResponse {
        private Long id;
        private KakaoUserAccount kakao_account;
    }

    @Getter
    private static class KakaoUserAccount {
        private Map<String, String> profile;
    }

    public Oauth2UserInfo login(LoginRequest loginRequest) {
        String accessToken = loginRequest.getAccessToken();
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        KakaoProfileResponse response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), KakaoProfileResponse.class)
                                                    .getBody();
        String id = response.getId().toString();
        String nickname = response.getKakao_account().getProfile().get("nickname");

        return Oauth2UserInfo.builder()
                             .providerId(id)
                             .name(nickname)
                             .build();
    }
}
