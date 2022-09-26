package com.Lommunity.controller.post;

import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.infrastructure.security.AuthUser;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponse createPosts(@RequestPart("dto") PostRequest postRequest,
                                    @RequestPart(required = false) List<MultipartFile> imageFiles,
                                    @AuthUser User user) {
        List<FileUploadRequest> fileUploadRequests = null;
        if (imageFiles != null) {
            fileUploadRequests = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                ensureImageFile(imageFile);
                fileUploadRequests.add(toFileUploadRequest(imageFile));
            }
        }
        return postService.createPost(postRequest, fileUploadRequests,user);
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

    private void ensureImageFile(MultipartFile profileImageFile) {
        if (!StringUtils.startsWith(profileImageFile.getContentType(), "image")) {
            throw new IllegalArgumentException("ContentType must start with image. ContentType: " + profileImageFile.getContentType());
        }
    }

    private FileUploadRequest toFileUploadRequest(MultipartFile multipartFile) {
        try {
            return FileUploadRequest.builder()
                                    .contentType(multipartFile.getContentType())
                                    .filename(multipartFile.getOriginalFilename())
                                    .bytes(multipartFile.getBytes())
                                    .size(multipartFile.getSize())
                                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid multipartFile", e);
        }
    }
}
