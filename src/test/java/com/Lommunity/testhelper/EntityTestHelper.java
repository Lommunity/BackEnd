package com.Lommunity.testhelper;

import com.Lommunity.application.comment.CommentService;
import com.Lommunity.application.comment.dto.request.CommentCreateRequest;
import com.Lommunity.application.comment.dto.response.CommentResponse;
import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.request.PostCreateRequest;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.request.RegisterRequest;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import com.Lommunity.infrastructure.security.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.Lommunity.domain.user.User.UserRole;
import static com.Lommunity.domain.user.User.builder;

@Component
public class EntityTestHelper {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    public User createUser(String nickname) {
        User user = userRepository.save(builder()
                .nickname(nickname)
                .profileImageUrl(null)
                .provider("naver")
                .providerId(UUID.randomUUID().toString())
                .role(UserRole.USER)
                .registered(false)
                .build());
        userService.register(RegisterRequest.builder()
                                            .userId(user.getId())
                                            .nickname(nickname)
                                            .regionCode(2611051000L)
                                            .build(), FileUploadRequest.builder().build());
        User registeredUser = userRepository.findWithRegionById(user.getId()).get();
        makeAuthenticationToken(user.getId());

        return registeredUser;
    }

    public PostResponse createPost(User user) {
        List<FileUploadRequest> fileUploadRequests = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fileUploadRequests.add(FileUploadRequest.builder()
                                                    .filename("fileName " + (i + 1))
                                                    .build());
        }
        return postService.createPost(PostCreateRequest.builder()
                                                       .topicId(1L)
                                                       .content("content")
                                                       .build(),
                fileUploadRequests,
                user);
    }

    public PostResponse createPostWithNumber(User user, int contentNumber) { // 한 명의 사용자가 다수개의 post를 게시할 때 사용하는 메서드
        List<FileUploadRequest> fileUploadRequests = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fileUploadRequests.add(FileUploadRequest.builder()
                                                    .filename("file name " + i + 1)
                                                    .build());
        }
        return postService.createPost(PostCreateRequest.builder()
                                                       .topicId(1L)
                                                       .content("content" + contentNumber)
                                                       .build(),
                fileUploadRequests
                , user);
    }

    public CommentResponse createComment(Long postId, String content, User user) {
        CommentCreateRequest createRequest = CommentCreateRequest.builder()
                                                                 .postId(postId)
                                                                 .content(content)
                                                                 .build();
        return commentService.createComment(createRequest, user);
    }

    private void makeAuthenticationToken(Long userId) {
        User user = userRepository.findWithRegionById(userId).get();
        Authentication authentication = new JwtAuthenticationToken(user, "jwt");
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
