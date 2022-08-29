package com.Lommunity.application.login;

import com.Lommunity.application.UserService;
import com.Lommunity.application.dto.user.UserDto;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final UserRepository userRepository;

    private final KakaoOauth2LoginService kakaoOauth2LoginService;
    private final NaverOauth2LoginService naverOauth2LoginService;

    public LoginResponse login(LoginRequest loginRequest) {
        Oauth2UserInfo oauth2UserInfo = getOauth2UserInfo(loginRequest); // provider에게서 얻은 정보를 담은 oauth2UserInfo
        User user = findUser(oauth2UserInfo, loginRequest.getProvider());
        return LoginResponse.builder()
                            .user(UserDto.fromEntity(user))
                            .jwt("jwt")
                            .build();
    }

    private Oauth2UserInfo getOauth2UserInfo(LoginRequest loginRequest) {
        if (loginRequest.getProvider().equals("kakao")) {
            return kakaoOauth2LoginService.login(loginRequest);
        } else if (loginRequest.getProvider().equals("naver")) {
            return naverOauth2LoginService.login(loginRequest);
        }

        throw new IllegalArgumentException("지원하지 않는 Provider 입니다. provider: " + loginRequest.getProvider());
    }

    private User findUser(Oauth2UserInfo oauth2UserInfo, String provider) {
        Optional<User> user = userRepository.findByProviderId(oauth2UserInfo.getProviderId());
        if (user.isPresent()) { // userRepository에 providerId에 해당하는 user 가 있는 경우(registered는 false 혹은 true)
            return user.get();
        }
        return null; // kakao 혹은 naver에서 넘겨주지 않는 도시와 같은 정보는 어떻게 해야할까?
    }

}
