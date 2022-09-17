package com.Lommunity.domain.user;

import com.Lommunity.application.user.UserService;
import com.Lommunity.domain.region.RegionRepository;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

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

    @Autowired
    EntityTestHelper entityTestHelper;

    @Test
    public void leftFetchJoinTest() {
        User hasRegion1 = entityTestHelper.createUser("홍길동");
        User hasRegion2 = entityTestHelper.createUser("감자");
        User noRegion = userRepository.save(builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId("0430")
                .role(User.UserRole.USER)
                .build());
        userRepository.save(noRegion);
        Optional<User> findUser1 = userRepository.findWithRegionById(hasRegion1.getId());
        Optional<User> findUser2 = userRepository.findWithRegionById(noRegion.getId());
        assertThat(findUser1.get().getNickname()).isEqualTo(hasRegion1.getNickname());
        assertThat(findUser2.get().getRegion()).isEqualTo(null);
    }

}