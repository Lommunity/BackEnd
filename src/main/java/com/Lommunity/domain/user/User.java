package com.Lommunity.domain.user;

import com.Lommunity.application.user.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users") // users로 설정을 안하면 오류 발생
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname; // Social 로그인을 통해 얻을 내용(naver에서 넘겨주는 name을 nickname으로 취급한다.)
    private String profileImageUrl;
    private String provider; // 회원가입 시 사용자가 수정불가한 부분
    private String providerId; // 회원가입 시 사용자가 수정불가한 부분
    private UserRole role; // 회원가입 시 사용자가 수정불가한 부분
    private boolean registered; // 회원가입 시 사용자가 수정불가한 부분
    private Long regionCode;

    public enum UserRole {
        USER, ADMIN
    }

    public void registerInfo(RegisterRequest registerRequest) {
        this.role = UserRole.USER;
        this.registered = true;
        this.nickname = registerRequest.getNickname();
        this.regionCode = registerRequest.getRegionCode();
        this.profileImageUrl = registerRequest.getProfileImageUrl();
    }

}
