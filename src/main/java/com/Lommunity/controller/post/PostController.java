package com.Lommunity.controller.post;

import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.*;
import com.Lommunity.domain.post.PostTopic;
import lombok.*;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public Page<PostDto> getUserPostsPage(@RequestParam(value = "userId") Long userId, Pageable pageable) {
        return postService.inquiryUserPosts(userId, pageable);
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
