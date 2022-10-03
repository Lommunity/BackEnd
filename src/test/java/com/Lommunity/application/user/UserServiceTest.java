package com.Lommunity.application.user;

import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.application.user.dto.RegisterResponse;
import com.Lommunity.application.user.dto.UserEditRequest;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static com.Lommunity.domain.user.User.UserRole;
import static com.Lommunity.domain.user.User.builder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    EntityTestHelper entityTestHelper;


    @Test
    public void register() {
        User user = userRepository.save(builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId(UUID.randomUUID().toString())
                .role(UserRole.USER)
                .registered(false)
                .build());

        RegisterResponse response = userService.register(RegisterRequest.builder()
                                                                        .userId(user.getId())
                                                                        .nickname("순대곱창전골")
                                                                        .regionCode(2611051000L)
                                                                        .build(),
                FileUploadRequest.builder()
                                 .filename("testImageUrl")
                                 .build());


        User completeJoin = userRepository.findWithRegionById(user.getId()).get();

        // Join 후
        assertThat(completeJoin.getId()).isEqualTo(user.getId());
        assertThat(completeJoin.isRegistered()).isEqualTo(true);
        assertThat(completeJoin.getProfileImageUrl()).isEqualTo("testImageUrl");
        assertThat(completeJoin.getRegion().getCode()).isEqualTo(2611051000L);
    }

    @Test
    public void edit() {
        // given
        User user = entityTestHelper.createUser("홍길동");

        // when
        UserEditRequest editRequest = UserEditRequest.builder()
                                                     .nickname("apple")
                                                     .profileImageUrl(user.getProfileImageUrl())
                                                     .regionCode(2611060000L).build();
        FileUploadRequest uploadRequest = FileUploadRequest.builder().filename("editImageFile").build();
        userService.edit(user.getId(), editRequest, uploadRequest);

        // then
        User findUser = userRepository.findWithRegionById(user.getId()).get();
        assertThat(findUser.getNickname()).isEqualTo("apple");
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getProfileImageUrl()).isEqualTo("editImageFile");
        assertThat(findUser.getRegion().getCode()).isEqualTo(2611060000L);
    }
}