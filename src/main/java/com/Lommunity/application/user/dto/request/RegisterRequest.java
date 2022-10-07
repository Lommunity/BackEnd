package com.Lommunity.application.user.dto.request;

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
