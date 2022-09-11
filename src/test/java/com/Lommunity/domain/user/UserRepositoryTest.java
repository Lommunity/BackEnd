package com.Lommunity.domain.user;

import com.Lommunity.application.user.UserService;
import com.Lommunity.domain.region.RegionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.Lommunity.domain.user.User.builder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    @Autowired
    RegionRepository regionRepository;

    @Test
    public void fetchJoinTest() {
        User uncompleteJoin = userRepository.save(builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId("0430")
                .role(User.UserRole.USER)
                .registered(false)
                .region(regionRepository.findRegionByCode(2611051000L).get())
                .build());

        User user = userRepository.findWithRegionById(uncompleteJoin.getId()).get();
        assertThat(user.getRegion().getCode()).isEqualTo(2611051000L);
        assertThat(user.getRegion().getFullname()).isEqualTo("부산 중구 중앙동");
    }

}