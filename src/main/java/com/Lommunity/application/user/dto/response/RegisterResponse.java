package com.Lommunity.application.user.dto.response;

import com.Lommunity.application.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterResponse {
    private UserDto user;
}
