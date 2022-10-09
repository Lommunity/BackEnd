package com.Lommunity.domain.comment;

import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.user.User;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    EntityTestHelper entityTestHelper;
    @Autowired
    CommentRepository commentRepository;


    @Test
    public void countByPostIdTest() {
        // given
        User user = entityTestHelper.registerUser("홍길동");
        Post post1 = entityTestHelper.createPostWithNumber(user, 1);
        Post post2 = entityTestHelper.createPostWithNumber(user, 2);
        Comment comment1 = entityTestHelper.createComment("content 1", post1, user);
        Comment comment2 = entityTestHelper.createComment("content 2", post1, user);
        Comment comment3 = entityTestHelper.createComment("content 3", post2, user);
        Comment comment4 = entityTestHelper.createComment("content 4", post2, user);
        Comment comment5 = entityTestHelper.createComment("content 5", post2, user);

        // when
        Long count1 = commentRepository.countByPostId(post1.getId());
        Long count2 = commentRepository.countByPostId(post2.getId());


        // then
        assertThat(count1).isEqualTo(2L);
        assertThat(count2).isEqualTo(3L);
    }
}