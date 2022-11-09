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
    private String nickname;
    private String profileImageUrl;
    private String provider;
    private String providerId;
    private UserRole role;
    private boolean registered;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code")
    private Region region;

    public enum UserRole {
        USER, ADMIN
    }

    public void registerInfo(String nickname, String profileImageUrl, Region region) {
        validate(nickname);
        this.role = UserRole.USER;
        this.registered = true;
        this.nickname = nickname;
        this.region = region;
        this.profileImageUrl = profileImageUrl;
    }

    public void editUserInfo(String nickname, String profileImage, Region region) {
        validate(nickname);
        this.nickname = nickname;
        this.profileImageUrl = profileImage;
        this.region = region;
    }

    private void validate(String nickname) {
        if (StringUtils.isEmpty(nickname)) throw new IllegalArgumentException("닉네임은 필수입니다.");
    }
}
