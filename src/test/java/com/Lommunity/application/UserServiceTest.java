package com.Lommunity.application;

import com.Lommunity.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void joinTest() {
        User user1 = User.builder()
                         .id(1L)
                         .name("이혜은")
                         .profileImgUrl("xxx")
                         .provider("xx")
                         .providerId("0430")
                         .role(User.UserRole.USER)
                         .registered(false)
                         .userNickname("순대")
                         .city("부산")
                         .gu("사상구")
                         .dong("주례동")
                         .build();
        User joinedUser = userService.join(user1);
        assertThat(user1.isRegistered()).isEqualTo(false);
        assertThat(joinedUser.isRegistered()).isEqualTo(true);
        assertThat(joinedUser.getUserNickname()).isEqualTo(user1.getUserNickname());
    }

    @Test
    public void loginTest() {
        User joinedUser = userService.join(User.builder()
                                               .id(1L)
                                               .name("이혜은")
                                               .profileImgUrl("xxx")
                                               .provider("xx")
                                               .providerId("0430")
                                               .role(User.UserRole.USER)
                                               .registered(false)
                                               .userNickname("순대")
                                               .city("부산")
                                               .gu("사상구")
                                               .dong("주례동")
                                               .build());
        User loginedUser = userService.login(joinedUser.getProviderId());
        assertThat(loginedUser.getId()).isEqualTo(joinedUser.getId());
        assertThat(loginedUser.getUserNickname()).isEqualTo(joinedUser.getUserNickname());
        assertThat(loginedUser.getCity()).isEqualTo("부산");

    }

}