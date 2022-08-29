package com.Lommunity.application.login;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Oauth2UserInfo {

    private String providerId;
    private String name;
    private String profileImageUrl;
}
