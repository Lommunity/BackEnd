package com.Lommunity.application.user.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

    private Long userId; // userId
    private String nickname;
    private String profileImageUrl;
    private String city;
    private String gu;
    private String dong;
}
