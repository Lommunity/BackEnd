package com.Lommunity.testHelper;

import com.Lommunity.application.post.PostService;
import com.Lommunity.application.post.dto.request.PostRequest;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import com.Lommunity.infrastructure.security.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
    PostRepository postRepository;
    @Autowired
    PostService postService;

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
                                            .profileImageUrl(null)
                                            .regionCode(2611051000L)
                                            .build());

        Authentication authentication = new JwtAuthenticationToken(user, "jwt");
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        return user;
    }

    public PostResponse createPost(Long userId) {
        return postService.createPost(PostRequest.builder()
                                                 .userId(userId)
                                                 .topicId(1L)
                                                 .content("content")
                                                 .build());
    }

    public PostResponse createPosts(Long userId, int contentNumber) {
        return postService.createPost(PostRequest.builder()
                                                 .userId(userId)
                                                 .topicId(1L)
                                                 .content("content" + contentNumber)
                                                 .build());
    }
}
