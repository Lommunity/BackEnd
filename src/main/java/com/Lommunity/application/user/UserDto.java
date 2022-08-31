package com.Lommunity.application.user;

import com.Lommunity.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import static com.Lommunity.domain.user.User.UserRole;

@Getter
@Builder
public class UserDto {

    private Long id;
    private String nickname; // Social 로그인을 통해 얻을 내용(naver에서 넘겨주는 name을 nickname으로 취급한다.)
    private String profileImageUrl;
    private String provider; // 회원가입 시 사용자가 수정불가한 부분
    private String providerId; // 회원가입 시 사용자가 수정불가한 부분
    private UserRole role; // 회원가입 시 사용자가 수정불가한 부분
    private boolean registered; // 회원가입 시 사용자가 수정불가한 부분
    private String city;
    private String gu;
    private String dong;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                      .id(user.getId())
                      .nickname(user.getNickname())
                      .profileImageUrl(user.getProfileImageUrl())
                      .provider(user.getProvider())
                      .providerId(user.getProviderId())
                      .role(user.getRole())
                      .registered(user.isRegistered())
                      .city(user.getCity())
                      .gu(user.getGu())
                      .dong(user.getDong())
                      .build();
    }
}
