package com.Lommunity.application.comment;

import com.Lommunity.application.comment.dto.CommentDto;
import com.Lommunity.application.comment.dto.request.CommentCreateRequest;
import com.Lommunity.application.comment.dto.request.CommentEditRequest;
import com.Lommunity.application.comment.dto.response.CommentPageResponse;
import com.Lommunity.application.comment.dto.response.CommentResponse;
import com.Lommunity.domain.comment.Comment;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponse createComment(CommentCreateRequest createRequest, User user) {
        Post post = isPresentPost(createRequest.getPostId());
        Comment comment = commentRepository.save(Comment.builder()
                                                        .post(post)
                                                        .user(user)
                                                        .content(createRequest.getContent())
                                                        .build());
        return CommentResponse.builder()
                              .comment(CommentDto.fromEntity(comment))
                              .build();
    }

    public CommentResponse editComment(Long commentId, CommentEditRequest editRequest, User user) {
        Comment comment = isPresentComment(commentId);
        isWriter(comment, user.getId());
        comment.editComment(editRequest.getContent());
        return CommentResponse.builder()
                              .comment(CommentDto.fromEntity(comment))
                              .build();
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = isPresentComment(commentId);
        isWriter(comment, user.getId());
        commentRepository.delete(comment);
    }

    public CommentPageResponse getCommentPage(Long postId, Pageable pageable) {
        isPresentPost(postId);
        Page<CommentDto> commentDtoPage = commentRepository.findCommentPageByPostId(postId, pageable)
                                                           .map(CommentDto::fromEntity);
        return CommentPageResponse.builder()
                                  .commentPage(commentDtoPage)
                                  .build();
    }

    private Post isPresentPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new IllegalArgumentException("postID에 해당하는 게시물이 존재하지 않습니다. PostID: " + postId));
    }

    private Comment isPresentComment(Long commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new IllegalArgumentException("commentID에 해당하는 코멘트가 존재하지 않습니다. CommentID: " + commentId));
    }

    private void isWriter(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("코멘트의 작성자가 아닙니다.");
        }
    }
}
