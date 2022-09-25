package com.Lommunity.application.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterResponse {
    private UserDto user;
}
