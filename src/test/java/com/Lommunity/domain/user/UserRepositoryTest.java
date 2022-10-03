package com.Lommunity.domain.user;

import com.Lommunity.application.user.UserService;
import com.Lommunity.domain.region.RegionRepository;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static com.Lommunity.domain.user.User.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                .providerId(UUID.randomUUID().toString())
                .role(User.UserRole.USER)
                .build());
        userRepository.save(noRegion);
        Optional<User> findUser1 = userRepository.findWithRegionById(hasRegion1.getId());
        Optional<User> findUser2 = userRepository.findWithRegionById(noRegion.getId());
        assertThat(findUser1.get().getNickname()).isEqualTo(hasRegion1.getNickname());
        assertThat(findUser2.get().getRegion()).isEqualTo(null);
    }

    @Test
    public void uniqueTest() {
        // given
        User user1 = builder()
                .nickname("홍길동")
                .profileImageUrl(null)
                .provider("naver")
                .providerId("1234")
                .role(User.UserRole.USER)
                .registered(false)
                .build();
        User user2 = builder()
                .nickname("김사과")
                .profileImageUrl(null)
                .provider("naver")
                .providerId("1234")
                .role(User.UserRole.USER)
                .registered(false)
                .build();
        // when
        userRepository.save(user1);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));
    }
}