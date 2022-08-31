package com.Lommunity.application.login.dto;

import com.Lommunity.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDto { // 로그인 후 provider에게 받은 user 정보를 담는다.
    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String provider;
    private boolean registered;

    public static UserLoginDto fromEntity(User user) {
        return UserLoginDto.builder()
                           .id(user.getId())
                           .nickname(user.getNickname())
                           .profileImageUrl(user.getProfileImageUrl())
                           .provider(user.getProvider())
                           .registered(user.isRegistered())
                           .build();
    }
}
