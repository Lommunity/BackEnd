package com.Lommunity.application.login;

import com.Lommunity.application.dto.user.UserDto;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import com.Lommunity.infrastructure.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final KakaoOauth2LoginService kakaoOauth2LoginService;
    private final NaverOauth2LoginService naverOauth2LoginService;
    private final JwtHelper jwtHelper;

    public LoginResponse login(LoginRequest loginRequest) {
        Oauth2UserInfo oauth2UserInfo = getOauth2UserInfo(loginRequest); // provider에게서 얻은 정보를 담은 oauth2UserInfo
        User user = findUser(oauth2UserInfo, loginRequest.getProvider());
        return LoginResponse.builder()
                            .user(UserDto.fromEntity(user))
                            .jwt(jwtHelper.createJwt(user))
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
        // SNS 로그인을 처음 해본 사용자
        return userRepository.save(User.builder()
                                       .name(oauth2UserInfo.getName())
                                       .profileImageUrl(oauth2UserInfo.getProfileImageUrl())
                                       .provider(provider)
                                       .providerId(oauth2UserInfo.getProviderId())
                                       .role(User.UserRole.USER)
                                       .registered(false)
                                       .build());
    }

}
