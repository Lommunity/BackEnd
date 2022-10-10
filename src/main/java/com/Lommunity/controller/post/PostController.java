package com.Lommunity.controller.post;

import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.PostTopicDto;
import com.Lommunity.application.post.dto.request.PostCreateRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.application.post.dto.response.PostTopicListResponse;
import com.Lommunity.domain.post.PostTopic;
import com.Lommunity.domain.user.User;
import com.Lommunity.infrastructure.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
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
    public PostResponse createPost(@RequestPart("dto") PostCreateRequest createRequest,
                                   @RequestPart(required = false) List<MultipartFile> postImageFiles,
                                   @AuthUser User user) {
        List<FileUploadRequest> fileUploadRequests = new ArrayList<>();
        if (!CollectionUtils.isEmpty(postImageFiles)) {
            for (MultipartFile imageFile : postImageFiles) {
                ensureImageFile(imageFile);
                fileUploadRequests.add(toFileUploadRequest(imageFile));
            }
        }
        return postService.createPost(createRequest, fileUploadRequests, user);
    }

    @PutMapping("/{postId}")
    private PostResponse editPost(@PathVariable("postId") Long postId,
                                  @RequestPart("dto") PostEditRequest editRequest,
                                  @RequestPart(required = false) List<MultipartFile> postImageFiles,
                                  @AuthUser User user) {
        if (CollectionUtils.isEmpty(editRequest.getPostImageUrls())) {
            editRequest.nullImageUrls();
        }
        List<FileUploadRequest> fileUploadRequests = new ArrayList<>();
        if (!CollectionUtils.isEmpty(postImageFiles)) {
            for (MultipartFile postImageFile : postImageFiles) {
                ensureImageFile(postImageFile);
                fileUploadRequests.add(toFileUploadRequest(postImageFile));
            }
        }
        return postService.editPost(postId, editRequest, fileUploadRequests, user);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable("postId") Long postId, @AuthUser User user) {
        postService.deletePost(postId, user);
    }


    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable("postId") Long postId, @AuthUser User user) {
        return postService.getPost(postId, user);
    }

    @GetMapping
    public PostPageResponse getPostPage(@RequestParam(value = "userId", required = false) Long userId,
                                        @RequestParam(value = "topicId", required = false) Long topicId,
                                        @RequestParam(value = "word", required = false) String word,
                                        @AuthUser User user,
                                        Pageable pageable) {
        if (userId != null) return postService.getPostPageByUserId(userId, user, pageable);
        if (topicId != null) return postService.getPostPageByTopicId(topicId, user, pageable);
        if (!StringUtils.isEmpty(word)) return postService.searchPost(word, user, pageable);
        return postService.getAllPostPage(user, pageable);
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
