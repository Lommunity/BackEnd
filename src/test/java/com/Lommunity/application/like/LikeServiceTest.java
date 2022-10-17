package com.Lommunity.application.like;

import com.Lommunity.application.like.dto.request.LikeRequest;
import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.like.LikeTarget;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.user.User;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LikeServiceTest {
    @Autowired
    EntityTestHelper entityTestHelper;
    @Autowired
    LikeService likeService;
    @Autowired
    PostService postService;

    @Test
    public void createLikeTest() {
        // given
        User user1 = entityTestHelper.userRegisterBusan("홍길동");
        User user2 = entityTestHelper.userRegisterBusan("apple");
        Post post = entityTestHelper.createPostWithNumber(user1, 1);

        // when
        LikeRequest likeRequest = LikeRequest.builder()
                                       .targetType(LikeTarget.POST)
                                       .targetId(post.getId())
                                       .build();
        likeService.createLike(likeRequest, user1);
        likeService.createLike(likeRequest, user2);
        PostResponse postResponse = postService.getPost(post.getId(), user1);

        // then
        assertThat(postResponse.getPost().getLikeCount()).isEqualTo(2L);
        assertThat(postResponse.getPost().isUserLike()).isEqualTo(true);
    }

    @Test
    public void createLikeOverlapTest() {
        // given
        User user1 = entityTestHelper.userRegisterBusan("홍길동");
        Post post = entityTestHelper.createPostWithNumber(user1, 1);

        // when
        LikeRequest likeRequest = LikeRequest.builder()
                                             .targetType(LikeTarget.POST)
                                             .targetId(post.getId())
                                             .build();
        likeService.createLike(likeRequest, user1);
        likeService.createLike(likeRequest, user1);
        PostResponse postResponse = postService.getPost(post.getId(), user1);

        // then
        assertThat(postResponse.getPost().getLikeCount()).isEqualTo(1L);
    }
}