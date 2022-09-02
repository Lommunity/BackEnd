package com.Lommunity.application.post;

import com.Lommunity.application.post.dto.*;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시물 작성
    public PostResponse create(PostRequest createRequest) {
        User user = isPresentUser(createRequest.getUserId());
        user.checkRegister();
        Post savePost = postRepository.save(Post.builder()
                                                .user(user)
                                                .title(createRequest.getTitle())
                                                .content(createRequest.getContent())
                                                .imageUrl(createRequest.getImageUrl())
                                                .createdBy(createRequest.getUserId())
                                                .lastModifiedBy(createRequest.getUserId())
                                                .createdDate(LocalDateTime.now())
                                                .lastModifiedDate(LocalDateTime.now())
                                                .build());
        return PostResponse.builder()
                           .post(PostDto.fromEntity(savePost))
                           .build();
    }

    // 게시물 수정
    public PostResponse editPost(PostEditRequest editRequest) {
        isPresentUser(editRequest.getUserId());
        Post post = isPresentPost(editRequest.getPostId());
        if (!post.getCreatedBy().equals(editRequest.getUserId())) {
            throw new IllegalArgumentException("userID에 해당하는 사용자는 이 게시물의 작성자가 아닙니다.");
        }
        post.editPost(editRequest.getUserId(), editRequest.getTitle(), editRequest.getContent(), editRequest.getImageUrl());
        return PostResponse.builder()
                           .post(PostDto.fromEntity(post))
                           .build();
    }

    // 게시물 삭제
    public void deletePost(PostDeleteRequest deleteRequest) {
        isPresentUser(deleteRequest.getUserId());
        Post post = isPresentPost(deleteRequest.getPostId());
        if (!post.getCreatedBy().equals(deleteRequest.getUserId())) {
            throw new IllegalArgumentException("userID에 해당하는 사용자는 이 게시물의 작성자가 아니기에 삭제가 불가능합니다.");
        }
        postRepository.delete(post);
    }

    public User isPresentUser(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new IllegalArgumentException("해당 userID에 해당하는 사용자는 존재하지 않습니다. userID: " + userId));
    }

    public Post isPresentPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new IllegalArgumentException("해당 postID에 해당하는 게시물은 존재하지 않습니다. userID: " + postId));
    }
}
