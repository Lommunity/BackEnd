package com.Lommunity.application.comment;

import com.Lommunity.application.comment.dto.request.CommentCreateRequest;
import com.Lommunity.application.comment.dto.response.CommentResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.comment.Comment;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    EntityTestHelper entityTestHelper;

    @Test
    public void createCommentTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostResponse post = entityTestHelper.createPost(user);

        // when
        CommentCreateRequest request = CommentCreateRequest.builder()
                                                           .postId(post.getPost().getPostId())
                                                           .content("comment content")
                                                           .build();
        CommentResponse commentResponse = commentService.createComment(request, user);
        // then
        Comment comment = commentRepository.findById(commentResponse.getComment().getCommentId()).get();
        assertThat(comment.getPost().getId()).isEqualTo(post.getPost().getPostId());
        assertThat(comment.getUser().getId()).isEqualTo(user.getId());
        assertThat(comment.getContent()).isEqualTo("comment content");
    }
}