package com.Lommunity.application.post;

import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.post.dto.request.PostCreateRequest;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
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
        PostCreateRequest createRequest = PostCreateRequest.builder()
                                                           .topicId(1L)
                                                           .content("content")
                                                           .build();
        List<FileUploadRequest> fileUploadRequests = new ArrayList<>();
        fileUploadRequests.add(FileUploadRequest.builder()
                                                .filename("fileName 1")
                                                .build());
        fileUploadRequests.add(FileUploadRequest.builder()
                                                .filename("fileName 2")
                                                .build());
        PostResponse postResponse = postService.createPost(createRequest, fileUploadRequests, user);

        // then
        assertThat(postResponse.getPost().getTopic().getTopicId()).isEqualTo(1L);
        assertThat(postResponse.getPost().getPostImageUrls().size()).isEqualTo(2);
        assertThat(postResponse.getPost().getPostImageUrls().get(0)).isEqualTo("fileName 1");
        assertThat(postResponse.getPost().getPostImageUrls().get(1)).isEqualTo("fileName 2");

    }

    @Test
    public void emptyContentCreateTest() {
        // then
        User user = entityTestHelper.createUser("홍길동");
        // given
        PostCreateRequest createRequest = PostCreateRequest.builder()
                                                           .topicId(1L)
                                                           .content("")
                                                           .build();
        List<FileUploadRequest> fileUploadRequests = new ArrayList<>();
        fileUploadRequests.add(FileUploadRequest.builder()
                                                .filename("fileName 1")
                                                .build());
        fileUploadRequests.add(FileUploadRequest.builder()
                                                .filename("fileName 2")
                                                .build());
        // when
        assertThrows(IllegalArgumentException.class, () -> postService.createPost(createRequest, fileUploadRequests, user));
    }

    @Test
    public void editPostTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostResponse postResponse = entityTestHelper.createPost(user);
        List<String> leavePostImageUrls = postResponse.getPost().getPostImageUrls().subList(0, 1);
        List<FileUploadRequest> newPostImageUrls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            newPostImageUrls.add(FileUploadRequest
                    .builder()
                    .filename("newFileName " + i)
                    .build());
        }
        // when
        PostEditRequest editRequest = PostEditRequest.builder()
                                                     .postId(postResponse.getPost().getPostId())
                                                     .topicId(3L)
                                                     .content("edit content")
                                                     .postImageUrls(leavePostImageUrls)
                                                     .build();
        postService.editPost(editRequest, newPostImageUrls, user);
        Post findPost = postRepository.findById(postResponse.getPost().getPostId()).get();
        // then
        assertThat(findPost.getTopicId()).isEqualTo(3L);
        assertThat(findPost.getContent()).isEqualTo("edit content");
        assertThat(findPost.getPostImageUrls().size()).isEqualTo(4);
        assertThat(findPost.getPostImageUrls().get(0)).isEqualTo("fileName 1");
        assertThat(findPost.getPostImageUrls().get(1)).isEqualTo("newFileName 1");
        assertThat(findPost.getPostImageUrls().get(2)).isEqualTo("newFileName 2");
    }

    @Test
    public void emptyContentEditTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostResponse postResponse = entityTestHelper.createPost(user);
        List<String> leavePostImageUrls = postResponse.getPost().getPostImageUrls().subList(0, 1);
        List<FileUploadRequest> newPostImageUrls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            newPostImageUrls.add(FileUploadRequest
                    .builder()
                    .filename("newFileName " + i)
                    .build());
        }

        // when
        PostEditRequest editRequest = PostEditRequest.builder()
                                                     .postId(postResponse.getPost().getPostId())
                                                     .topicId(3L)
                                                     .content("")
                                                     .postImageUrls(leavePostImageUrls)
                                                     .build();
        // when
        assertThrows(IllegalArgumentException.class, () -> postService.editPost(editRequest, newPostImageUrls, user));

    }

    @Test
    public void deletePostTest() {
        // given
        User user = entityTestHelper.createUser("홍길동");
        PostDto createdPost = entityTestHelper.createPost(user).getPost();
        // when
        PostDeleteRequest deleteRequest = PostDeleteRequest.builder()
                                                           .postId(createdPost.getPostId())
                                                           .build();
        postService.deletePost(deleteRequest, user);
        // then
        assertThrows(NoSuchElementException.class, () -> postRepository.findById(createdPost.getPostId())
                                                                       .orElseThrow(() -> new NoSuchElementException("postId에 해당하는 게시물은 없습니다.")));
    }

    @Test
    public void getPost() {
        // give
        User user = entityTestHelper.createUser("홍길동");
        PostResponse postResponse1 = entityTestHelper.createPostWithNumber(user, 1);
        PostResponse postResponse2 = entityTestHelper.createPostWithNumber(user, 2);
        PostResponse postResponse3 = entityTestHelper.createPostWithNumber(user, 3);
        // when
        PostResponse getPostById = postService.getPost(postResponse1.getPost().getPostId());
        // then
        assertThat(getPostById.getPost().getPostId()).isEqualTo(postResponse1.getPost().getPostId());
        assertThat(getPostById.getPost().getContent()).isEqualTo("content1");
    }

    @Test
    public void allPostsByPageTest() {
        // given
        postRepository.deleteAll();
        User user = entityTestHelper.createUser("홍길동");

        List<PostDto> originPostDtoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            originPostDtoList.add(entityTestHelper.createPostWithNumber(user, (i + 1)).getPost());
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
            entityTestHelper.createPostWithNumber(user1, (i + 1));
        }

        User user2 = entityTestHelper.createUser("이혜은");
        for (int i = 0; i < 6; i++) {
            entityTestHelper.createPostWithNumber(user2, (i + 1));
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