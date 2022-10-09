package com.Lommunity.application.like;

import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.response.PostResponse;
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
        User user1 = entityTestHelper.registerUser("홍길동");
        User user2 = entityTestHelper.registerUser("apple");
        Post post = entityTestHelper.createPostWithNumber(user1, 1);

        // when
        likeService.createLike(post.getId(), user1);
        likeService.createLike(post.getId(), user2);
        PostResponse postResponse = postService.getPost(post.getId());

        // then
        assertThat(postResponse.getPost().getLikeCount()).isEqualTo(2L);
        assertThat(postResponse.getPost().isWriterLike()).isEqualTo(true);
    }

    @Test
    public void createLikeOverlapTest() {
        // given
        User user1 = entityTestHelper.registerUser("홍길동");
        Post post = entityTestHelper.createPostWithNumber(user1, 1);

        // when
        likeService.createLike(post.getId(), user1);
        likeService.createLike(post.getId(), user1);
        PostResponse postResponse = postService.getPost(post.getId());

        // then
        assertThat(postResponse.getPost().getLikeCount()).isEqualTo(1L);
    }
}