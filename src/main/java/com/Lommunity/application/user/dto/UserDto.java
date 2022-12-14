package com.Lommunity.application.user.dto;

import com.Lommunity.application.region.dto.RegionDto;
import com.Lommunity.domain.user.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.Lommunity.domain.user.User.UserRole;

@Getter
@Builder
@EqualsAndHashCode
public class UserDto {

    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private UserRole role;
    private boolean registered;
    private RegionDto region;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                      .userId(user.getId())
                      .nickname(user.getNickname())
                      .profileImageUrl(user.getProfileImageUrl())
                      .role(user.getRole())
                      .registered(user.isRegistered())
                      .region(user.getRegion() == null ? null : RegionDto.fromEntity(user.getRegion()))
                      .build();
    }

}
