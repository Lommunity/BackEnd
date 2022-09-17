package com.Lommunity.controller.post;

import com.Lommunity.annotation.AuthUser;
import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.PostTopicDto;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.request.PostRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.application.post.dto.response.PostTopicListResponse;
import com.Lommunity.domain.post.PostTopic;
import com.Lommunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponse createPosts(@RequestBody PostRequest postRequest, @AuthUser User user) {
        return postService.createPost(postRequest, user);
    }

    @PutMapping
    private PostResponse editPost(@RequestBody PostEditRequest editRequest, @AuthUser User user) {
        return postService.editPost(editRequest, user);
    }

    @GetMapping
    public PostPageResponse getPostsByPage(@RequestParam(value = "userId", required = false) Long userId, Pageable pageable, @AuthUser User user) {
        if (userId == null) {
            return postService.allPostsByPage(pageable);
        }
        return postService.userPostsByPage(userId, pageable, user);
    }

    @DeleteMapping
    public void deletePost(@RequestBody PostDeleteRequest deleteRequest, @AuthUser User user) {
        postService.deletePost(deleteRequest, user);
    }

    @GetMapping("/post-topics")
    public PostTopicListResponse getPostTopics() {
        List<PostTopicDto> topics = Arrays.stream(PostTopic.values())
                                          .map(PostTopicDto::fromPostTopic)
                                          .collect(Collectors.toList());
        return PostTopicListResponse.builder()
                                    .topics(topics)
                                    .build();
    }
}
