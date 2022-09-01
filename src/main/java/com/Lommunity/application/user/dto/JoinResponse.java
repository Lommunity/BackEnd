package com.Lommunity.application.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinResponse {
    private UserDto user;
}
