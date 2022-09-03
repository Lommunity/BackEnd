package com.Lommunity.controller.post;

import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.post.dto.PostRequest;
import com.Lommunity.application.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
}
