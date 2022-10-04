package com.Lommunity.domain.post;

import com.Lommunity.domain.region.Region;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.UUID;

import static com.Lommunity.domain.user.User.builder;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    public void findWithUserByIdTest() {
        Region region = Region.builder()
                              .code(2611051000L)
                              .level(3L)
                              .parentCode(2611000000L)
                              .fullname("부산 중구 중앙동")
                              .build();
        User user = userRepository.save(builder()
                .nickname("홍길동")
                .profileImageUrl(null)
                .provider("naver")
                .providerId(UUID.randomUUID().toString())
                .role(User.UserRole.USER)
                .region(region)
                .registered(false)
                .build());
        Post post = postRepository.save(Post.builder()
                                            .user(user)
                                            .topicId(1L)
                                            .content("content")
                                            .postImageUrls(new ArrayList<>())
                                            .build());
        Post fetchJoinPost = postRepository.findWithUserById(post.getId()).get();
    }
}