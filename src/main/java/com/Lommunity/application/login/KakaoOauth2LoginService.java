package com.Lommunity.application.login;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoOauth2LoginService {

    @Getter
    private static class KakaoProfileResponse {
        private Long id;
        private KakaoUserAccount kakaoUserAccount;
    }

    @Getter
    private static class KakaoUserAccount {
        private Map<String, String> profile;
    }

    private RestTemplate restTemplate = new RestTemplate();

    public Oauth2UserInfo login(LoginRequest loginRequest) {
        String accessToken = loginRequest.getAccessToken();
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        KakaoProfileResponse response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), KakaoProfileResponse.class)
                                                    .getBody();
        String id = response.getId().toString();
        String nickname = response.getKakaoUserAccount().getProfile().get("nickname");

        return Oauth2UserInfo.builder()
                             .providerId(id)
                             .name(nickname)
                             .build();
    }
}
