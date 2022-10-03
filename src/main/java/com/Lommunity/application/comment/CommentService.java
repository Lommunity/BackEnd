package com.Lommunity.application.comment;

import com.Lommunity.application.comment.dto.CommentDto;
import com.Lommunity.application.comment.dto.request.CommentCreateRequest;
import com.Lommunity.application.comment.dto.request.CommentEditRequest;
import com.Lommunity.application.comment.dto.response.CommentResponse;
import com.Lommunity.domain.comment.Comment;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponse createComment(CommentCreateRequest createRequest, User user) {
        Post post = postRepository.findById(createRequest.getPostId())
                                  .orElseThrow(() -> new IllegalArgumentException("postId에 해당하는 게시물이 없습니다. PostID: " + createRequest.getPostId()));
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
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new IllegalArgumentException("commentId에 해당하는 코멘트가 없습니다. CommentID: " + commentId));
        isWriter(comment, user.getId());
        comment.editComment(editRequest.getContent());
        return CommentResponse.builder()
                              .comment(CommentDto.fromEntity(comment))
                              .build();
    }

    private void isWriter(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("코멘트의 작성자가 아닙니다.");
        }
    }
}
