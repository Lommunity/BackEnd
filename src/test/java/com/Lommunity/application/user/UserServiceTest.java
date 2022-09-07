package com.Lommunity.application.user;

import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.domain.region.Region;
import com.Lommunity.domain.region.RegionRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.Lommunity.domain.user.User.UserRole;
import static com.Lommunity.domain.user.User.builder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    RegionRepository regionRepository;

    @Test
    public void joinTest() {
        User uncompleteJoin = userRepository.save(builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId("0430")
                .role(UserRole.USER)
                .registered(false)
                .build());

        // Join 하기 전
        assertThat(uncompleteJoin.getId()).isEqualTo(1);
        assertThat(uncompleteJoin.isRegistered()).isEqualTo(false);
        assertThat(uncompleteJoin.getProfileImageUrl()).isEqualTo("aaa");

        userService.register(RegisterRequest.builder()
                                            .id(uncompleteJoin.getId())
                                            .nickname("순대곱창전골")
                                            .profileImageUrl(null)
                                            .regionCode(2611051000L)
                                            .build());


        User completeJoin = userRepository.findById(uncompleteJoin.getId()).get();

        // Join 후
        assertThat(completeJoin.getId()).isEqualTo(uncompleteJoin.getId());
        assertThat(completeJoin.isRegistered()).isEqualTo(true);
        assertThat(completeJoin.getProfileImageUrl()).isEqualTo(null);
        assertThat(regionRepository.findRegionByCode(completeJoin.getRegionCode()).get().getFullname()).isEqualTo("부산 중구 중앙동");
    }
}