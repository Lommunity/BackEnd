package com.Lommunity.application.user.dto;

import com.Lommunity.application.user.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinResponse {
    private UserDto user;
}
