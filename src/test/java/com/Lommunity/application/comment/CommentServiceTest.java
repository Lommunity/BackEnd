package com.Lommunity.application.comment;

import com.Lommunity.application.comment.dto.CommentDto;
import com.Lommunity.application.comment.dto.request.CommentCreateRequest;
import com.Lommunity.application.comment.dto.request.CommentEditRequest;
import com.Lommunity.application.comment.dto.response.CommentPageResponse;
import com.Lommunity.application.comment.dto.response.CommentResponse;
import com.Lommunity.domain.comment.Comment;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.post.Post;
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
        User user = entityTestHelper.userRegisterBusan("홍길동");
        Post post = entityTestHelper.createPost(user);

        // when
        CommentCreateRequest request = CommentCreateRequest.builder()
                                                           .postId(post.getId())
                                                           .content("comment content")
                                                           .build();
        CommentResponse commentResponse = commentService.createComment(request, user);
        // then
        Comment comment = commentRepository.findWithUserByCommentId(commentResponse.getComment().getCommentId()).get();
        assertThat(comment.getUser().getNickname()).isEqualTo("홍길동");
        assertThat(comment.getUser().getRegion().getFullname()).isEqualTo("부산 중구 중앙동");
        assertThat(comment.getContent()).isEqualTo("comment content");
    }

    @Test
    public void emptyContentCreateTest() {
        // given
        User user = entityTestHelper.userRegisterBusan("홍길동");
        Post post = entityTestHelper.createPost(user);

        // when
        CommentCreateRequest request = CommentCreateRequest.builder()
                                                           .postId(post.getId())
                                                           .content("")
                                                           .build();
        // when
        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(request, user));
    }

    @Test
    public void editCommentTest() {
        // given
        User user = entityTestHelper.userRegisterBusan("홍길동");
        Post post = entityTestHelper.createPost(user);
        Long postId = post.getId();
        Comment comment = entityTestHelper.createComment("comment content", post, user);
        // when
        CommentEditRequest editRequest = CommentEditRequest.builder()
                                                           .content("edit comment content")
                                                           .build();
        commentService.editComment(comment.getId(), editRequest, user);
        Comment findComment = commentRepository.findById(comment.getId()).get();
        // then
        assertThat(findComment.getContent()).isEqualTo("edit comment content");
    }

    @Test
    public void deleteCommentTest() {
        // given
        User user = entityTestHelper.userRegisterBusan("홍길동");
        Post post = entityTestHelper.createPost(user);
        Long postId = post.getId();
        Comment comment = entityTestHelper.createComment("comment content", post, user);

        // when
        commentService.deleteComment(comment.getId(), user);

        // then
        assertThat(commentRepository.existsById(comment.getId())).isEqualTo(false);
    }

    @Test
    public void getCommentPageTest() {
        // given
        commentRepository.deleteAll();
        User user1 = entityTestHelper.userRegisterBusan("포도");
        User user2 = entityTestHelper.userRegisterBusan("사과");
        Post post1 = entityTestHelper.createPost(user1);
        Post post2 = entityTestHelper.createPost(user2);


        // when
        List<CommentDto> originCommentDtoList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Comment comment = entityTestHelper.createComment("comment content " + i, post1, user1);
            originCommentDtoList.add(CommentDto.fromEntity(comment));
        }
        for (int i = 6; i <= 10; i++) {
            Comment comment = entityTestHelper.createComment("comment content " + i, post2, user2);
            originCommentDtoList.add(CommentDto.fromEntity(comment));
        }
        originCommentDtoList.sort(((o1, o2) -> {
            if (o1.getLastModifiedDate().isAfter(o2.getLastModifiedDate())) return 1;
            else return -1;
        }));
        PageRequest pageable = PageRequest.of(0, 5);
        CommentPageResponse commentPage = commentService.getCommentPage(post1.getId(), pageable);
        List<CommentDto> contentPageToList = commentPage.getCommentPage().getContent();

        // then
        for (int i = 0; i < 5; i++) {
            assertThat(contentPageToList).isEqualTo(originCommentDtoList.subList(0, 5));
        }
    }
}