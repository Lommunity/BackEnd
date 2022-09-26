package com.Lommunity.application.post;

import com.Lommunity.application.file.FileService;
import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.post.dto.request.PostDeleteRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.request.PostRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostRepository;
import com.Lommunity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private static final String POST_IMAGE_DIRECTORY = "post";

    private final PostRepository postRepository;
    private final FileService fileService;

    // 게시물 작성
    public PostResponse createPost(PostRequest createRequest,
                                   List<FileUploadRequest> fileUploadRequests,
                                   User user) {
        validateUser(createRequest.getUserId(), user);

        List<String> imageUrlsList = null;
        if (fileUploadRequests != null) {
            imageUrlsList = new ArrayList<>();
            for (FileUploadRequest fileUploadRequest : fileUploadRequests) {
                String imageUrl = fileService.upload(fileUploadRequest, POST_IMAGE_DIRECTORY);
                imageUrlsList.add(imageUrl);
            }
        }



        Post savePost = postRepository.save(Post.builder()
                                                .user(user)
                                                .topicId(createRequest.getTopicId())
                                                .content(createRequest.getContent())
                                                .imageUrls(imageUrlsList)
                                                .build());
        return PostResponse.builder()
                           .post(PostDto.fromEntity(savePost))
                           .build();
    }

    // 전체 게시물 목록 조회
    public PostPageResponse allPostsByPage(Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        Page<PostDto> postDtoPage = all.map(PostDto::fromEntity);
        return PostPageResponse.builder()
                               .postPage(postDtoPage)
                               .build();

    }

    // 작성자별 게시물 목록 조회 → Pagination
    public PostPageResponse userPostsByPage(Long userId, Pageable pageable, User user) {
        validateUser(userId, user);
        Page<PostDto> postDtoPageByuserId = postRepository.findByUserId(userId, pageable)
                                                          .map(PostDto::fromEntity);
        return PostPageResponse.builder()
                               .postPage(postDtoPageByuserId)
                               .build();
    }

    // 게시물 수정
    public PostResponse editPost(PostEditRequest editRequest, User user) {
        validateUser(editRequest.getUserId(), user);

        Post post = isPresentPost(editRequest.getPostId());
        if (!post.getCreatedBy().equals(editRequest.getUserId())) {
            throw new IllegalArgumentException("userID에 해당하는 사용자는 이 게시물의 작성자가 아닙니다.");
        }
        post.editPost(editRequest.getUserId(), editRequest.getTopicId(), editRequest.getContent());
        postRepository.save(post);
        return PostResponse.builder()
                           .post(PostDto.fromEntity(post))
                           .build();
    }

    // 게시물 삭제
    public void deletePost(PostDeleteRequest deleteRequest, User user) {
        validateUser(deleteRequest.getUserId(), user);
        Post post = isPresentPost(deleteRequest.getPostId());
        if (!post.getCreatedBy().equals(deleteRequest.getUserId())) {
            throw new IllegalArgumentException("userID에 해당하는 사용자는 이 게시물의 작성자가 아니기에 삭제가 불가능합니다.");
        }
        postRepository.delete(post);
    }

    private static void validateUser(Long userId, User user) {
        if (user == null || !userId.equals(user.getId())) {
            throw new IllegalArgumentException("User 정보가 일치하지 않습니다.");
        }
    }

    public Post isPresentPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new IllegalArgumentException("해당 postID에 해당하는 게시물은 존재하지 않습니다. userID: " + postId));
    }
}
