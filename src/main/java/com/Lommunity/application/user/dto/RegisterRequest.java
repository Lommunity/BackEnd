package com.Lommunity.application.user.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private Long userId; // userId
    private String nickname;
    private Long regionCode;
}
