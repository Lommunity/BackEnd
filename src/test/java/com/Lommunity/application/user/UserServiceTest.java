package com.Lommunity.application.user;

import com.Lommunity.application.user.dto.JoinRequest;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.Lommunity.domain.user.User.UserRole;
import static com.Lommunity.domain.user.User.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

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

        assertThatThrownBy(uncompleteJoin::checkRegister)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원가입을 하지 않은 사용자 입니다.");

        // Join 하기 전
        assertThat(uncompleteJoin.getId()).isEqualTo(1);
        assertThat(uncompleteJoin.isRegistered()).isEqualTo(false);
        assertThat(uncompleteJoin.getProfileImageUrl()).isEqualTo("aaa");
        assertThat(uncompleteJoin.getCity()).isEqualTo(null);

        userService.join(JoinRequest.builder()
                                    .userId(uncompleteJoin.getId())
                                    .nickname("순대곱창전골")
                                    .profileImageUrl(null)
                                    .city("부산")
                                    .gu("사상구")
                                    .dong("주례동")
                                    .build());


        User completeJoin = userRepository.findById(uncompleteJoin.getId()).get();

        // Join 후
        assertThat(completeJoin.getId()).isEqualTo(uncompleteJoin.getId());
        assertThat(completeJoin.isRegistered()).isEqualTo(true);
        assertThat(completeJoin.getProfileImageUrl()).isEqualTo(null);
        assertThat(completeJoin.getCity()).isEqualTo("부산");
    }
}