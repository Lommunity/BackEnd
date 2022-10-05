package com.Lommunity.application.comment;

import com.Lommunity.application.comment.dto.CommentDto;
import com.Lommunity.application.comment.dto.request.CommentCreateRequest;
import com.Lommunity.application.comment.dto.request.CommentEditRequest;
import com.Lommunity.application.comment.dto.response.CommentPageResponse;
import com.Lommunity.application.comment.dto.response.CommentResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.comment.Comment;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
//        assertThat(comment.getPost().getContent()).isEqualTo(post.getPost().getContent());
        assertThat(comment.getUser().getId()).isEqualTo(user.getId());
        assertThat(comment.getContent()).isEqualTo("comment content");
    }

    @Test
    public void emptyContentCreateTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostResponse post = entityTestHelper.createPost(user);

        // when
        CommentCreateRequest request = CommentCreateRequest.builder()
                                                           .postId(post.getPost().getPostId())
                                                           .content("")
                                                           .build();
        // when
        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(request, user));
    }

    @Test
    public void editCommentTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostResponse post = entityTestHelper.createPost(user);
        Long postId = post.getPost().getPostId();
        CommentResponse comment_content = entityTestHelper.createComment(postId, "comment content", user);
        Long commentId = comment_content.getComment().getCommentId();
        // when
        CommentEditRequest editRequest = CommentEditRequest.builder()
                                                           .content("edit comment content")
                                                           .build();
        commentService.editComment(commentId, editRequest, user);
        Comment findComment = commentRepository.findById(commentId).get();
        // then
        assertThat(findComment.getContent()).isEqualTo("edit comment content");
    }

    @Test
    public void deleteCommentTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostResponse post = entityTestHelper.createPost(user);
        Long postId = post.getPost().getPostId();
        CommentResponse comment_content = entityTestHelper.createComment(postId, "comment content", user);
        Long commentId = comment_content.getComment().getCommentId();

        // when
        commentService.deleteComment(commentId, user);
        // then
        assertThat(commentRepository.existsById(commentId)).isEqualTo(false);
    }

    @Test
    public void getCommentPageTest() {
        // given
        commentRepository.deleteAll();
        User user1 = entityTestHelper.createUser("포도");
        User user2 = entityTestHelper.createUser("사과");
        PostResponse postResponse1 = entityTestHelper.createPost(user1);
        Long postId1 = postResponse1.getPost().getPostId();
        PostResponse postResponse2 = entityTestHelper.createPost(user2);
        Long postId2 = postResponse2.getPost().getPostId();

        // when
        List<CommentDto> comments = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            comments.add(entityTestHelper.createComment(postId1, "content " + i, user1).getComment());
        }
        for (int i = 7; i <= 9; i++) {
            comments.add(entityTestHelper.createComment(postId2, "content " + i, user2).getComment());
        }
        PageRequest pageable1 = PageRequest.of(1, 3);
        PageRequest pageable2 = PageRequest.of(0, 3);
        CommentPageResponse commentPage1 = commentService.getCommentPage(postId1, pageable1);
        CommentPageResponse commentPage2 = commentService.getCommentPage(postId2, pageable2);

        // then
        assertThat(commentPage1.getCommentPage().getContent()).isEqualTo(comments.subList(3, 6));
        assertThat(commentPage2.getCommentPage().getContent()).isEqualTo(comments.subList(6, 9));
    }
}