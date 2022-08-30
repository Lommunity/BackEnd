package com.Lommunity.domain.user;

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
    private String name; // Social 로그인을 통해 얻을 내용
    private String profileImageUrl;
    private String provider;
    private String providerId;
    private UserRole role;
    private boolean registered;
    private String userNickname; // 회원 가입시 따로 입력
    private String city;
    private String gu;
    private String dong;

    public enum UserRole {
        USER, ADMIN
    }

    public void register() {
        this.registered = true;
    }

}
