package com.Lommunity.application.dto.user;

import com.Lommunity.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String provider;
    private boolean registered;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                      .id(user.getId())
                      .nickname(user.getNickname())
                      .profileImageUrl(user.getProfileImageUrl())
                      .provider(user.getProvider())
                      .registered(user.isRegistered())
                      .build();
    }
}
