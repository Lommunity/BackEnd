package com.Lommunity.application.post;

import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.post.PostTopic;
import com.Lommunity.domain.user.User;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityTestHelper entityTestHelper;

    @Test
    public void createPostTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");

        // when
        PostResponse postResponse = entityTestHelper.createPost(user);

        // then
        assertThat(PostTopic.findTopicById(postResponse.getPost().getTopic().getTopicId()).name()).isEqualTo("QUESTION");
        assertThat(postResponse.getPost().getTopic().getDescription()).isEqualTo("동네 질문");
        assertThat(postResponse.getPost().getImageUrl()).isEqualTo(null);

    }

    @Test
    public void editPostTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostResponse postResponse = entityTestHelper.createPost(user);
        // when
        PostEditRequest editRequest = PostEditRequest.builder()
                                                     .userId(user.getId())
                                                     .topicId(3L)
                                                     .postId(postResponse.getPost().getPostId())
                                                     .content(null)
                                                     .imageUrl("aaa")
                                                     .build();
        postService.editPost(editRequest, user);
        Post post = postRepository.findById(postResponse.getPost().getPostId()).get();
        // then
        assertThat(post.getTopicId()).isEqualTo(3L);
        assertThat(post.getCreatedBy()).isEqualTo(user.getId());
        assertThat(post.getContent()).isEqualTo("content"); // null을 넘길경우 수정 안된다.
        assertThat(post.getImageUrl()).isEqualTo("aaa");
    }

    @Test
    public void deletePostTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostDto createdPost = entityTestHelper.createPost(user).getPost();
        Long postId = createdPost.getPostId();
        // when
        PostDeleteRequest deleteRequest = PostDeleteRequest.builder()
                                                           .userId(user.getId())
                                                           .postId(createdPost.getPostId())
                                                           .build();
        postService.deletePost(deleteRequest, user);
        // then
        assertThrows(NoSuchElementException.class, () -> postRepository.findById(postId)
                                                                       .orElseThrow(() -> new NoSuchElementException("postId에 해당하는 게시물은 없습니다.")));
    }

    @Test
    public void allPostsByPageTest() {
        // given
        postRepository.deleteAll();
        User user = entityTestHelper.createUser("홍길동");

        List<PostDto> originPostDtoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            originPostDtoList.add(entityTestHelper.createPosts(user, (i + 1)).getPost());
        }
        // when
        PageRequest pageable = PageRequest.of(1, 5);
        PostPageResponse allPostsPageResponse = postService.allPostsByPage(pageable);

        // then
        Page<PostDto> postPage = allPostsPageResponse.getPostPage();
        List<PostDto> postDtoList = postPage.getContent();
        assertThat(postDtoList).isEqualTo(originPostDtoList.subList(5, 10));
    }

    @Test
    public void userPostsByPageTest() {
        // given
        User user1 = entityTestHelper.createUser("홍길동");

        for (int i = 0; i < 5; i++) {
            entityTestHelper.createPosts(user1, (i + 1));
        }

        User user2 = entityTestHelper.createUser("이혜은");
        for (int i = 0; i < 6; i++) {
            entityTestHelper.createPosts(user2, (i + 1));
        }

        // when
        PageRequest pageable1 = PageRequest.of(0, 5);
        PageRequest pageable2 = PageRequest.of(1, 3);
        PostPageResponse userPostsPageResponse1 = postService.userPostsByPage(user1.getId(), pageable1, user1);
        PostPageResponse userPostsPageResponse2 = postService.userPostsByPage(user2.getId(), pageable2, user2);

        // then
        assertThat(userPostsPageResponse1.getPostPage().getTotalPages()).isEqualTo(1);
        assertThat(userPostsPageResponse2.getPostPage().getTotalPages()).isEqualTo(2);

    }
}