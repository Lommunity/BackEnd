package com.Lommunity.domain.post;

import com.Lommunity.domain.region.Region;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.UUID;

import static com.Lommunity.domain.user.User.builder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityTestHelper entityTestHelper;


    @Test
    public void findPostByPostId() {
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
        Post fetchJoinPost = postRepository.findWithUserByPostId(post.getId()).get();
        assertThat(fetchJoinPost.getUser().getRegion().getFullname()).isEqualTo("부산 중구 중앙동");
    }

    @Test
    public void findPostPageByTopicId() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        Post post1 = postRepository.save(Post.builder()
                                            .user(user)
                                            .topicId(1L)
                                            .content("content1")
                                            .postImageUrls(new ArrayList<>())
                                            .build());
        Post post2 = postRepository.save(Post.builder()
                                            .user(user)
                                            .topicId(2L)
                                            .content("content2")
                                            .postImageUrls(new ArrayList<>())
                                            .build());
        Post post3 = postRepository.save(Post.builder()
                                            .user(user)
                                            .topicId(2L)
                                            .content("content3")
                                            .postImageUrls(new ArrayList<>())
                                            .build());

        // when
        Page<Post> postPageByTopicId = postRepository.findPostPageByTopicId(2L, PageRequest.of(0, 2));

        // then
        assertThat(postPageByTopicId.getSize()).isEqualTo(2);
        assertThat(postPageByTopicId.getContent().get(0).getTopicId()).isEqualTo(2L);
        assertThat(postPageByTopicId.getContent().get(1).getTopicId()).isEqualTo(2L);
        assertThat(postPageByTopicId.getContent().get(0).getContent()).isEqualTo("content2");
        assertThat(postPageByTopicId.getContent().get(1).getContent()).isEqualTo("content3");

    }
}