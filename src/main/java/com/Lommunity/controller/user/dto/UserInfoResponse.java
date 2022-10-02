package com.Lommunity.controller.user.dto;

import com.Lommunity.application.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    UserDto user;
}
