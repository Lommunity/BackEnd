package com.Lommunity.controller.post;

import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.*;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.request.PostRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.application.post.dto.response.PostTopicListResponse;
import com.Lommunity.domain.post.PostTopic;
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
    public PostResponse createPosts(@RequestBody PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @PutMapping
    private PostResponse editPost(@RequestBody PostEditRequest editRequest) {
        return postService.editPost(editRequest);
    }

    @GetMapping
    public PostPageResponse getPostsByPage(@RequestParam(value = "userId", required = false) Long userId, Pageable pageable) {
        if (userId == null) {
            return postService.allPostsByPage(pageable);
        }
        return postService.userPostsByPage(userId, pageable);
    }

    @DeleteMapping
    public void deletePost(@RequestBody PostDeleteRequest deleteRequest) {
        postService.deletePost(deleteRequest);
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
