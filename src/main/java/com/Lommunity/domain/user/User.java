package com.Lommunity.domain.user;

import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.domain.region.Region;
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
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "PROVIDER_PROVIDERID_UNIQUE", columnNames = {"provider", "providerId"}
)})
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code")
    private Region region;

    public enum UserRole {
        USER, ADMIN
    }

    public void registerInfo(RegisterRequest registerRequest, String profileImageUrl, Region region) {
        this.role = UserRole.USER;
        this.registered = true;
        this.nickname = registerRequest.getNickname();
        this.region = region;
        this.profileImageUrl = profileImageUrl;
    }

    public void checkRegister() {
        if (!this.isRegistered()) {
            throw new IllegalArgumentException("회원가입을 하지 않은 사용자 입니다.");
        }
    }

    public void editUserInfo(String newNickname, String newProfileImageUrl, Region newRegion) {
        if (newNickname != null) {
            this.nickname = newNickname;
        }
        if (newRegion != null) {
            this.region = newRegion;
        }
        this.profileImageUrl = newProfileImageUrl;
    }

}
