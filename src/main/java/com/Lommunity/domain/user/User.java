package com.Lommunity.domain.user;

import com.Lommunity.application.user.dto.JoinRequest;
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
    private String city;
    private String gu;
    private String dong;

    public enum UserRole {
        USER, ADMIN
    }

    public void userJoin(JoinRequest joinRequest) {
        this.role = UserRole.USER;
        this.registered = true;
        this.nickname = joinRequest.getNickname();
        this.profileImageUrl = joinRequest.getProfileImageUrl();
        this.city = joinRequest.getCity();
        this.gu = joinRequest.getGu();
        this.dong = joinRequest.getDong();
    }

}
