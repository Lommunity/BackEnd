package com.Lommunity.application.like;

import com.Lommunity.application.like.dto.LikeDto;
import com.Lommunity.application.like.dto.response.LikeResponse;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.like.Like;
import com.Lommunity.domain.like.LikeRepository;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    protected final CommentRepository commentRepository;

    public LikeResponse createLike(Long postId, User user) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new IllegalArgumentException("postID에 해당하는 게시물이 존재하지 않습니다. postID: " + postId));
        Long commentCount = commentRepository.countByPostId(postId);
        Like like = likeRepository.save(Like.builder()
                                            .user(user)
                                            .post(post)
                                            .build());
        Long likeCount = likeRepository.countByPostId(postId);
        boolean isWriterLike = likeRepository.existsByPostIdAndUserId(postId, post.getUser().getId());
        return LikeResponse.builder()
                           .like(LikeDto.fromEntity(like, commentCount, likeCount, isWriterLike))
                           .build();
    }
}
