package com.Lommunity.application.user.dto.request;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditRequest {
    private String nickname;
    private Long regionCode;
    private String profileImageUrl;
}
