package com.Lommunity.application.post;

import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.request.PostRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.post.PostTopic;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
        userService.register(RegisterRequest.builder()
                                            .userId(user.getId())
                                            .nickname("순대곱창전골")
                                            .profileImageUrl(null)
                                            .regionCode(2611051000L)
                                            .build());

        // when
        PostResponse postResponse = postService.createPost(PostRequest.builder()
                                                                      .userId(user.getId())
                                                                      .topicId(1L)
                                                                      .content("content 1")
                                                                      .build());
        // then
        assertThat(PostTopic.findTopicById(postResponse.getPost().getTopic().getTopicId()).name()).isEqualTo("QUESTION");
        assertThat(postResponse.getPost().getTopic().getDescription()).isEqualTo("동네 질문");
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
        userService.register(RegisterRequest.builder()
                                            .userId(user.getId())
                                            .nickname("순대곱창전골")
                                            .profileImageUrl(null)
                                            .regionCode(2611051000L)
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
        userService.register(RegisterRequest.builder()
                                            .userId(user.getId())
                                            .nickname("순대곱창전골")
                                            .profileImageUrl(null)
                                            .regionCode(2611051000L)
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

    @Test
    public void allPostsByPageTest() {
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
        userService.register(RegisterRequest.builder()
                                            .userId(user.getId())
                                            .nickname("순대곱창전골")
                                            .profileImageUrl(null)
                                            .regionCode(2611051000L)
                                            .build());

        for (int i = 0; i < 10; i++) {
            postService.createPost(PostRequest.builder()
                                              .userId(user.getId())
                                              .topicId(1L)
                                              .content("content " + (i + 1))
                                              .build()).getPost();
        }
        // when
        PageRequest pageable = PageRequest.of(1, 5);
        PostPageResponse allPostsPageResponse = postService.allPostsByPage(pageable);

        // then
        Page<PostDto> postPage = allPostsPageResponse.getPostPage();
        assertThat(postPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void userPostsByPageTest() {
        // given
        User user1 = userRepository.save(User
                .builder()
                .nickname("이혜은")
                .profileImageUrl("aaa")
                .provider("naver")
                .providerId("0430")
                .role(UserRole.USER)
                .registered(false)
                .build());
        userService.register(RegisterRequest.builder()
                                            .userId(user1.getId())
                                            .nickname("순대곱창전골")
                                            .profileImageUrl(null)
                                            .regionCode(2611051000L)
                                            .build());

        User user2 = userRepository.save(User
                .builder()
                .nickname("홍길동")
                .profileImageUrl("bbb")
                .provider("kakao")
                .providerId("2022")
                .role(UserRole.USER)
                .registered(false)
                .build());
        userService.register(RegisterRequest.builder()
                                            .userId(user2.getId())
                                            .nickname(null)
                                            .profileImageUrl("123")
                                            .regionCode(2611051000L)
                                            .build());


        for (int i = 0; i < 5; i++) {
            postService.createPost(PostRequest.builder()
                                              .userId(user1.getId())
                                              .topicId(1L)
                                              .content("user1 content " + (i + 1))
                                              .build()).getPost();
        }
        for (int i = 0; i < 6; i++) {
            postService.createPost(PostRequest.builder()
                                              .userId(user2.getId())
                                              .topicId(2L)
                                              .content("user2 content " + (i + 1))
                                              .build()).getPost();
        }
        // when
        PageRequest pageable1 = PageRequest.of(0, 5);
        PageRequest pageable2 = PageRequest.of(1, 3);
        PostPageResponse userPostsPageResponse1 = postService.userPostsByPage(user1.getId(), pageable1);
        PostPageResponse userPostsPageResponse2 = postService.userPostsByPage(user2.getId(), pageable2);

        // then
        assertThat(userPostsPageResponse1.getPostPage().getTotalPages()).isEqualTo(1);
        assertThat(userPostsPageResponse2.getPostPage().getTotalPages()).isEqualTo(2);

    }
}