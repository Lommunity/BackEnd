package com.Lommunity.domain.user;

import com.Lommunity.domain.region.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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

    public void registerInfo(String nickname, String profileImageUrl, Region region) {
        emptyCheck(nickname);
        this.role = UserRole.USER;
        this.registered = true;
        this.nickname = nickname;
        this.region = region;
        this.profileImageUrl = profileImageUrl;
    }

    public void editUserInfo(String nickname, String profileImage, Region region) {
        emptyCheck(nickname);
        this.nickname = nickname;
        this.profileImageUrl = profileImage;
        this.region = region;
    }

    private void emptyCheck(String nickname) {
        if (StringUtils.isEmpty(nickname)) throw new IllegalArgumentException("닉네임은 필수입니다.");
    }
}
