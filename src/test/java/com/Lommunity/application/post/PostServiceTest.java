package com.Lommunity.application.post;

import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.post.dto.request.PostCreateRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.comment.Comment;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityTestHelper entityTestHelper;

    @Test
    public void createPostTest() {
        // given
        User user = entityTestHelper.registerUser("홍길동");

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
        User user = entityTestHelper.registerUser("홍길동");
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
        User user = entityTestHelper.registerUser("홍길동");
        Post post = entityTestHelper.createPost(user);
        List<String> leavePostImageUrls = post.getPostImageUrls().subList(0, 1);
        List<FileUploadRequest> newPostImageUrls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            newPostImageUrls.add(FileUploadRequest
                    .builder()
                    .filename("newFileName " + i)
                    .build());
        }
        // when
        PostEditRequest editRequest = PostEditRequest.builder()
                                                     .topicId(3L)
                                                     .content("edit content")
                                                     .postImageUrls(leavePostImageUrls)
                                                     .build();
        postService.editPost(post.getId(), editRequest, newPostImageUrls, user);
        Post findPost = postRepository.findById(post.getId()).get();
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
        User user = entityTestHelper.registerUser("홍길동");
        Post post = entityTestHelper.createPost(user);
        List<String> leavePostImageUrls = post.getPostImageUrls().subList(0, 1);
        List<FileUploadRequest> newPostImageUrls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            newPostImageUrls.add(FileUploadRequest
                    .builder()
                    .filename("newFileName " + i)
                    .build());
        }

        // when
        PostEditRequest editRequest = PostEditRequest.builder()
                                                     .topicId(3L)
                                                     .content("")
                                                     .postImageUrls(leavePostImageUrls)
                                                     .build();
        // when
        assertThrows(IllegalArgumentException.class, () -> postService.editPost(post.getId(), editRequest, newPostImageUrls, user));

    }

    @Test
    public void deletePostTest() {
        // given
        User user = entityTestHelper.registerUser("홍길동");
        Post post = entityTestHelper.createPost(user);

        // when

        postService.deletePost(post.getId(), user);
        // then
        assertThrows(NoSuchElementException.class, () -> postRepository.findById(post.getId())
                                                                       .orElseThrow(() -> new NoSuchElementException("postId에 해당하는 게시물은 없습니다.")));
    }

    @Test
    public void getPost() {
        // give
        User user = entityTestHelper.registerUser("홍길동");
        Post post1 = entityTestHelper.createPostWithNumber(user, 1);

        // when
        PostResponse getPostById = postService.getPost(post1.getId());
        // then
        assertThat(getPostById.getPost().getPostId()).isEqualTo(post1.getId());
        assertThat(getPostById.getPost().getContent()).isEqualTo("content1");
    }

    @Test
    public void allPostsByPageTest() {
        // given
        commentRepository.deleteAll();
        postRepository.deleteAll();
        User user = entityTestHelper.registerUser("홍길동");

        List<PostDto> originPostDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Post post = entityTestHelper.createPostWithNumber(user, i);
            originPostDtoList.add(PostDto.fromEntity(post));
        }

        // when
        PageRequest pageable = PageRequest.of(1, 5);
        PostPageResponse allPostPage = postService.getAllPostPage(pageable);

        // then
        List<PostDto> postDtoList = allPostPage.getPostPage().getContent();
        for (int i = 4; i >= 0; i--) {
            assertThat(postDtoList.get(4 - i)).isEqualTo(originPostDtoList.subList(0, 5).get(i));
        }
    }

    @Test
    public void userPostsByPageTest() {
        // given
        User user1 = entityTestHelper.registerUser("홍길동");

        for (int i = 0; i < 5; i++) {
            entityTestHelper.createPostWithNumber(user1, (i + 1));
        }

        User user2 = entityTestHelper.registerUser("이혜은");
        for (int i = 0; i < 6; i++) {
            entityTestHelper.createPostWithNumber(user2, (i + 1));
        }

        // when
        PageRequest pageable1 = PageRequest.of(0, 5);
        PageRequest pageable2 = PageRequest.of(1, 3);
        PostPageResponse userPostsPageResponse1 = postService.getPostPageByUserId(user1.getId(), pageable1);
        PostPageResponse userPostsPageResponse2 = postService.getPostPageByUserId(user2.getId(), pageable2);

        // then
        assertThat(userPostsPageResponse1.getPostPage().getTotalPages()).isEqualTo(1);
        assertThat(userPostsPageResponse2.getPostPage().getTotalPages()).isEqualTo(2);

    }

    @Test
    public void postDtoWithCommentCountTest() {
        // given
        commentRepository.deleteAll();
        postRepository.deleteAll();
        User postWriter = entityTestHelper.registerUser("홍길동");
        User commentWriter = entityTestHelper.registerUser("김");
        Post post1 = entityTestHelper.createPostWithNumber(postWriter, 1);
        Post post2 = entityTestHelper.createPostWithNumber(postWriter, 2);
        Comment comment1 = entityTestHelper.createComment("comment content", post1, commentWriter);
        Comment comment2 = entityTestHelper.createComment("comment content", post1, commentWriter);
        Comment comment3 = entityTestHelper.createComment("comment content", post2, commentWriter);

        // when
        PostPageResponse postPageResponse = postService.getPostPageByUserId(postWriter.getId(), PageRequest.of(0, 3));

        // then
        PostDto actualPost1 = postPageResponse.getPostPage().getContent().get(0);
        assertThat(actualPost1.getPostId()).isEqualTo(post2.getId());
        assertThat(actualPost1.getCommentCount()).isEqualTo(1L);
        PostDto actualPost2 = postPageResponse.getPostPage().getContent().get(1);
        assertThat(actualPost2.getPostId()).isEqualTo(post1.getId());
        assertThat(actualPost2.getCommentCount()).isEqualTo(2L);
    }

    @Test
    public void search() {
        // given
        User user = entityTestHelper.registerUser("홍길동");
        Post post1 = entityTestHelper.createPostWithNumber(user, 1);
        Post post2 = entityTestHelper.createPostWithNumber(user, 12);
        Post post3 = entityTestHelper.createPostWithNumber(user, 41);
        Post post4 = entityTestHelper.createPostWithNumber(user, 2);

        // when
        PostPageResponse postPageResponse = postService.searchPost("1", PageRequest.of(0, 4));
        List<PostDto> postDtoList = postPageResponse.getPostPage().getContent();
        for (PostDto postDto : postDtoList) {
            System.out.println(postDto.getContent());
        }
        // then
        assertThat(postDtoList.get(0).getContent()).isEqualTo("content41");
        assertThat(postDtoList.get(1).getContent()).isEqualTo("content12");
        assertThat(postDtoList.get(2).getContent()).isEqualTo("content1");
        assertThat(postDtoList.contains(PostDto.fromEntity(post4))).isEqualTo(false);
    }
}

