package com.Lommunity.application.user;

import com.Lommunity.application.user.dto.JoinRequest;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.Lommunity.domain.user.User.UserRole;
import static com.Lommunity.domain.user.User.builder;

@SpringBootTest
@Transactional
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

        // Join 하기 전
        System.out.println("X, id: " + uncompleteJoin.getId());
        System.out.println("X, registered: " + uncompleteJoin.isRegistered());
        System.out.println("X, profile: " + uncompleteJoin.getProfileImageUrl());
        System.out.println("X, city:" + uncompleteJoin.getCity());

        userService.join(JoinRequest.builder()
                                    .id(uncompleteJoin.getId())
                                    .nickname("순대곱창전골")
                                    .profileImageUrl(null)
                                    .city("부산")
                                    .gu("사상구")
                                    .dong("주례동")
                                    .build());


        User completeJoin = userRepository.findById(uncompleteJoin.getId()).get();

        // Join 후
        System.out.println("O, id: " + completeJoin.getId());
        System.out.println("O, registered: " + completeJoin.isRegistered());
        System.out.println("O, profile: " + completeJoin.getProfileImageUrl());
        System.out.println("O, city:" + completeJoin.getCity());
    }
}