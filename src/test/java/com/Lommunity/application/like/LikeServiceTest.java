package com.Lommunity.application.like;

import com.Lommunity.application.like.dto.response.LikeResponse;
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

    @Test
    public void createLikeTest() {
        // given
        User user = entityTestHelper.registerUser("홍길동");
        Post post = entityTestHelper.createPostWithNumber(user, 1);

        // when
        LikeResponse likeReponse = likeService.createLike(post.getId(), user);

        // then
        assertThat(likeReponse.getLike().getUser().getNickname()).isEqualTo("홍길동");
        assertThat(likeReponse.getLike().getPost().getContent()).isEqualTo("content1");
    }
}