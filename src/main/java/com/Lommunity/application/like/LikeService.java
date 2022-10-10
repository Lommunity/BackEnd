package com.Lommunity.application.like;

import com.Lommunity.application.like.dto.request.LikeRequest;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.like.Like;
import com.Lommunity.domain.like.LikeRepository;
import com.Lommunity.domain.like.LikeTarget;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    protected final CommentRepository commentRepository;

    public void createLike(LikeRequest likeRequest, User user) {
        if (!isAlreadyLike(likeRequest.getTargetType(), likeRequest.getTargetId(), user.getId())) {
            likeRepository.save(Like.builder()
                                    .user(user)
                                    .targetType(likeRequest.getTargetType())
                                    .targetId(likeRequest.getTargetId())
                                    .build());
        }

    }

    public void deleteLike(LikeRequest likeRequest, User user) {
        Optional<Like> like = likeRepository
                .findByTargetTypeAndTargetIdAndUserId(likeRequest.getTargetType(), likeRequest.getTargetId(), user.getId());
        like.ifPresent(likeRepository::delete);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new IllegalArgumentException("postID에 해당하는 게시물이 존재하지 않습니다. postID: " + postId));
    }

    private boolean isAlreadyLike(LikeTarget targetType, Long targetId, Long userId) {
        return likeRepository.existsByTargetTypeAndTargetIdAndUserId(targetType, targetId, userId);
    }
}
