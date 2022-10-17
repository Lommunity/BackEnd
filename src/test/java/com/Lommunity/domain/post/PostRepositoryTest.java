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
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
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
        User userBusan = entityTestHelper.userRegisterBusan("홍길동");
        User userSeoul = entityTestHelper.userRegisterSeoul("김서울");

        Post post1 = postRepository.save(Post.builder()
                                             .user(userBusan)
                                             .topicId(1L)
                                             .content("content1")
                                             .postImageUrls(new ArrayList<>())
                                             .secondRegionLevel(userBusan.getRegion().getParentCode())
                                             .thirdRegionLevel(userBusan.getRegion().getCode())
                                             .build());
        Post post2 = postRepository.save(Post.builder()
                                             .user(userBusan)
                                             .topicId(2L)
                                             .content("content2")
                                             .postImageUrls(new ArrayList<>())
                                             .secondRegionLevel(userBusan.getRegion().getParentCode())
                                             .thirdRegionLevel(userBusan.getRegion().getCode())
                                             .build());
        Post post3 = postRepository.save(Post.builder()
                                             .user(userBusan)
                                             .topicId(2L)
                                             .content("content3")
                                             .postImageUrls(new ArrayList<>())
                                             .secondRegionLevel(userBusan.getRegion().getParentCode())
                                             .thirdRegionLevel(userBusan.getRegion().getCode())
                                             .build());
        Post post4 = postRepository.save(Post.builder()
                                             .user(userSeoul)
                                             .topicId(2L)
                                             .content("content seoul 1")
                                             .postImageUrls(new ArrayList<>())
                                             .secondRegionLevel(userSeoul.getRegion().getParentCode())
                                             .thirdRegionLevel(userSeoul.getRegion().getCode())
                                             .build());

        // when
        System.out.println(userBusan.getRegion().getParentCode());
        Page<Post> postPageByTopicId1 = postRepository.findPostPageBySecondRegionLevelAndTopicId(userBusan.getRegion().getParentCode(), 2L, PageRequest.of(0, 2));
        Page<Post> postPageByTopicId2 = postRepository.findPostPageBySecondRegionLevelAndTopicId(userSeoul.getRegion().getParentCode(), 2L, PageRequest.of(0, 1));

        // then
        assertThat(postPageByTopicId1.getSize()).isEqualTo(2);
        assertThat(postPageByTopicId2.getSize()).isEqualTo(1);
        assertThat(postPageByTopicId1.getContent().get(0).getTopicId()).isEqualTo(2L);
        assertThat(postPageByTopicId1.getContent().get(1).getTopicId()).isEqualTo(2L);
        assertThat(postPageByTopicId1.getContent().get(0).getContent()).isEqualTo("content2");
        assertThat(postPageByTopicId1.getContent().get(1).getContent()).isEqualTo("content3");
        assertThat(postPageByTopicId2.getContent().get(0).getContent()).isEqualTo("content seoul 1");

    }

    @Test
    public void postSearchTest() {
        // given

        User userBusan = entityTestHelper.userRegisterBusan("홍길동");
        User userSeoul = entityTestHelper.userRegisterSeoul("김서울");
        Post post1 = entityTestHelper.createPostWithNumber(userBusan, 1);
        Post post2 = entityTestHelper.createPostWithNumber(userBusan, 11);
        Post post3 = entityTestHelper.createPostWithNumber(userBusan, 22);
        Post post4 = entityTestHelper.createPostWithNumber(userSeoul, 31);

        // when
        Page<Post> posts = postRepository.findPostBySecondRegionLevelAndWord(userBusan.getRegion().getParentCode(), "1", PageRequest.of(0, 2, Sort.by("lastModifiedDate").descending()));
        List<Post> postList = posts.getContent();
        // then
        assertThat(posts.getSize()).isEqualTo(2);
        assertThat(postList.get(0).getContent()).isEqualTo("content11");
        assertThat(postList.get(1).getContent()).isEqualTo("content1");
    }
}