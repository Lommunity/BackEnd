package com.Lommunity.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.Lommunity.domain.user.User.UserRole;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {

    private UserDto user;
    private UserRole role;
    private String userNickname;
    private String city;
    private String gu;
    private String dong;
}
