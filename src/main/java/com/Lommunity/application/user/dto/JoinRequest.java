package com.Lommunity.application.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class JoinRequest {

    private Long id; // userId
    private String nickname;
    private String profileImageUrl;
    private String city;
    private String gu;
    private String dong;
}
