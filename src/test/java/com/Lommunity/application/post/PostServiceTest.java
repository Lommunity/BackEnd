package com.Lommunity.application.post;

import com.Lommunity.application.post.dto.*;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.request.PostRequest;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.JoinRequest;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.post.PostTopic;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static com.Lommunity.domain.user.User.UserRole;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void createPostTest() {
        // given
        User user = userRepository.save(User
                .builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId("0430")
                .role(UserRole.USER)
                .registered(false)
                .build());
        userService.join(JoinRequest.builder()
                                    .userId(user.getId())
                                    .nickname("순대곱창전골")
                                    .profileImageUrl(null)
                                    .city("부산")
                                    .gu("사상구")
                                    .dong("주례동")
                                    .build());

        // when
        PostResponse postResponse = postService.createPost(PostRequest.builder()
                                                                      .userId(user.getId())
                                                                      .topicId(1L)
                                                                      .content("content 1")
                                                                      .build());
        // then
//        System.out.println(user.getId());
//        System.out.println(postResponse.getPost().getPostId());
//        assertThat(postResponse.getPost().getPostId()).isEqualTo(1L);
        assertThat(PostTopic.findTopicById(postResponse.getPost().getTopicId()).name()).isEqualTo("QUESTION");
        System.out.println(postResponse.getPost().getCreatedBy());
//        assertThat(postResponse.getPost().getCreatedBy()).isEqualTo(user.getId());
        assertThat(postResponse.getPost().getImageUrl()).isEqualTo(null);

    }

    @Test
    public void editPostTest() {
        // given
        User user = userRepository.save(User
                .builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId("0430")
                .role(UserRole.USER)
                .registered(false)
                .build());
        userService.join(JoinRequest.builder()
                                    .userId(user.getId())
                                    .nickname("순대곱창전골")
                                    .profileImageUrl(null)
                                    .city("부산")
                                    .gu("사상구")
                                    .dong("주례동")
                                    .build());
        PostResponse createResponse = postService.createPost(PostRequest.builder()
                                                                        .userId(user.getId())
                                                                        .topicId(1L)
                                                                        .content("content 1")
                                                                        .build());
        // when
        PostEditRequest editRequest = PostEditRequest.builder()
                                                     .userId(user.getId())
                                                     .topicId(3L)
                                                     .postId(createResponse.getPost().getPostId())
                                                     .content(null)
                                                     .imageUrl("aaa")
                                                     .build();
        postService.editPost(editRequest);
        Post post = postRepository.findById(createResponse.getPost().getPostId()).get();
        // then
        assertThat(post.getTopicId()).isEqualTo(3L);
        assertThat(post.getContent()).isEqualTo("content 1"); // null을 넘길경우 수정 안된다.
        assertThat(post.getImageUrl()).isEqualTo("aaa");
    }

    @Test
    public void deletePostTest() {
        // given
        User user = userRepository.save(User
                .builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId("0430")
                .role(UserRole.USER)
                .registered(false)
                .build());
        userService.join(JoinRequest.builder()
                                    .userId(user.getId())
                                    .nickname("순대곱창전골")
                                    .profileImageUrl(null)
                                    .city("부산")
                                    .gu("사상구")
                                    .dong("주례동")
                                    .build());
        PostDto createdPost = postService.createPost(PostRequest.builder()
                                                                .userId(user.getId())
                                                                .topicId(2L)
                                                                .content("content 1")
                                                                .build()).getPost();
        Long postId = createdPost.getPostId();
        // when
        PostDeleteRequest deleteRequest = PostDeleteRequest.builder()
                                                           .userId(user.getId())
                                                           .postId(createdPost.getPostId())
                                                           .build();
        postService.deletePost(deleteRequest);
        // then
        assertThrows(NoSuchElementException.class, () -> postRepository.findById(postId)
                                                                       .orElseThrow(() -> new NoSuchElementException("postId에 해당하는 게시물은 없습니다.")));
    }
}