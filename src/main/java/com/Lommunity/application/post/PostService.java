package com.Lommunity.application.post;

import com.Lommunity.application.post.dto.*;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.request.PostRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시물 작성
    public PostResponse createPost(PostRequest createRequest) {
        User user = isPresentUser(createRequest.getUserId());
        user.checkRegister();
        Post savePost = postRepository.save(Post.builder()
                                                .user(user)
                                                .topicId(createRequest.getTopicId())
                                                .content(createRequest.getContent())
                                                .imageUrl(createRequest.getImageUrl())
                                                .build());
        return PostResponse.builder()
                           .post(PostDto.fromEntity(savePost))
                           .build();
    }

    // 전체 게시물 목록 조회
    public PostPageResponse allPostsByPage(Pageable pageable) {
        Page<PostDto> postDtoPage = postRepository.findAll(pageable)
                                                  .map(PostDto::fromEntity);
        return PostPageResponse.builder()
                               .postDtoPage(postDtoPage)
                               .build();

    }

    // 작성자별 게시물 목록 조회 → Pagination
    public PostPageResponse userPostsByPage(Long userId, Pageable pageable) {
        isPresentUser(userId);
        Page<PostDto> postDtoPageByuserId = postRepository.findByUserId(userId, pageable)
                                                          .map(PostDto::fromEntity);
        return PostPageResponse.builder()
                               .postDtoPage(postDtoPageByuserId)
                               .build();
    }

    // 게시물 수정
    public PostResponse editPost(PostEditRequest editRequest) {
        isPresentUser(editRequest.getUserId());
        Post post = isPresentPost(editRequest.getPostId());
        if (!post.getCreatedBy().equals(editRequest.getUserId())) {
            throw new IllegalArgumentException("userID에 해당하는 사용자는 이 게시물의 작성자가 아닙니다.");
        }
        post.editPost(editRequest.getUserId(), editRequest.getTopicId(), editRequest.getContent(), editRequest.getImageUrl());
        postRepository.save(post);
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
