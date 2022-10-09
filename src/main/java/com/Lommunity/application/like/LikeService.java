package com.Lommunity.application.like;

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

    public void createLike(Long postId, User user) {
        if (!isAlreadyLike(postId, user.getId())) {
            Like like = likeRepository.save(Like.builder()
                                                .user(user)
                                                .post(findPost(postId))
                                                .build());
        }
    }

    public void deleteLike(Long postId, User user) {
        if (isAlreadyLike(postId, user.getId())) {
            likeRepository.delete(likeRepository.findByPostIdAndUserId(postId, user.getId()).get());
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new IllegalArgumentException("postID에 해당하는 게시물이 존재하지 않습니다. postID: " + postId));
    }

    private boolean isAlreadyLike(Long postId, Long userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }
}
