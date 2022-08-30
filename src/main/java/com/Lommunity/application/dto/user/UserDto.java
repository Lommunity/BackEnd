package com.Lommunity.application.dto.user;

import com.Lommunity.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String profileImageUrl;
    private String provider;
    private String providerId;
    private boolean registered;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                      .id(user.getId())
                      .name(user.getName())
                      .profileImageUrl(user.getProfileImageUrl())
                      .provider(user.getProvider())
                      .providerId(user.getProviderId())
                      .registered(user.isRegistered())
                      .build();
    }
}
