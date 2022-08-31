package com.Lommunity.application.login;

import com.Lommunity.application.login.dto.LoginRequest;
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
public class NaverOauth2LoginService {

    private final RestTemplate restTemplate;

    @Getter
    private static class NaverProfileResponse {
        private String resultcode;
        private String message;
        private Map<String, String> response;
    }

    public Oauth2UserInfo login(LoginRequest loginRequest) {
        String accessToken = loginRequest.getAccessToken();
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        NaverProfileResponse response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), NaverProfileResponse.class)
                                                    .getBody();

        String id = response.getResponse().get("id");
        String name = response.getResponse().get("name");
        String profileImageUrl = response.getResponse().get("profile_image");

        return Oauth2UserInfo.builder()
                             .providerId(id)
                             .name(name)
                             .profileImageUrl(profileImageUrl)
                             .build();
    }
}
