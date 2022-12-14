package com.Lommunity.testhelper;

import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.request.RegisterRequest;
import com.Lommunity.domain.comment.Comment;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.like.Like;
import com.Lommunity.domain.like.LikeRepository;
import com.Lommunity.domain.like.LikeTarget;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
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
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    LikeRepository likeRepository;

    public User userRegisterBusan(String nickname) {
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

    public User userRegisterSeoul(String nickname) {
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
                                            .regionCode(1111010100L)
                                            .build(), FileUploadRequest.builder().build());
        User registeredUser = userRepository.findWithRegionById(user.getId()).get();
        makeAuthenticationToken(user.getId());

        return registeredUser;
    }

    public Post createPost(User user) {
        List<String> postImageUrls = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            postImageUrls.add("fileName " + i);
        }

        return postRepository.save(Post.builder()
                                       .user(user)
                                       .topicId(1L)
                                       .content("content")
                                       .postImageUrls(postImageUrls)
                                       .secondLevelRegionCode(user.getRegion().getParentCode())
                                       .thirdLevelRegionCode(user.getRegion().getCode())
                                       .build());
    }

    public Post createPostWithNumber(User user, int contentNumber) { // ??? ?????? ???????????? ???????????? post??? ????????? ??? ???????????? ?????????
        List<String> postImageUrls = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            postImageUrls.add("fileName " + i);
        }
        return postRepository.save(Post.builder()
                                       .user(user)
                                       .topicId(1L)
                                       .content("content" + contentNumber)
                                       .postImageUrls(postImageUrls)
                                       .secondLevelRegionCode(user.getRegion().getParentCode())
                                       .thirdLevelRegionCode(user.getRegion().getCode())
                                       .build());
    }

    public Comment createComment(String content, Post post, User user) {
        return commentRepository.save(Comment.builder()
                                             .post(post)
                                             .user(user)
                                             .content(content)
                                             .build());
    }

    public Like createLike(User user, LikeTarget targetType, Long targetId) {
        return likeRepository.save(Like.builder()
                                       .user(user)
                                       .targetType(targetType)
                                       .targetId(targetId)
                                       .build());
    }

    private void makeAuthenticationToken(Long userId) {
        User user = userRepository.findWithRegionById(userId).get();
        Authentication authentication = new JwtAuthenticationToken(user, "jwt");
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
