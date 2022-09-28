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
import org.springframework.util.CollectionUtils;

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

        List<String> imageUrlList = new ArrayList<>();
        checkImageNumber(fileUploadRequests.size());
        for (FileUploadRequest fileUploadRequest : fileUploadRequests) {
            String imageUrl = fileService.upload(fileUploadRequest, POST_IMAGE_DIRECTORY);
            imageUrlList.add(imageUrl);
        }


        Post savePost = postRepository.save(Post.builder()
                                                .user(user)
                                                .topicId(createRequest.getTopicId())
                                                .content(createRequest.getContent())
                                                .imageUrls(imageUrlList)
                                                .build());
        return PostResponse.builder()
                           .post(PostDto.fromEntity(savePost))
                           .build();
    }

    // 단일 게시물 조회
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("postID에 해당하는 게시물이 존재하지 않습니다. postID: " + postId));
        return PostResponse.builder()
                           .post(PostDto.fromEntity(post))
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
    public PostPageResponse userPostsByPage(Long userId, Pageable pageable) { // userId 없애야 하나 ?

        Page<PostDto> postDtoPageByuserId = postRepository.findByUserId(userId, pageable)
                                                          .map(PostDto::fromEntity);
        return PostPageResponse.builder()
                               .postPage(postDtoPageByuserId)
                               .build();
    }

    // 게시물 수정
    public PostResponse editPost(PostEditRequest editRequest,
                                 List<FileUploadRequest> fileUploadRequests,
                                 User user) {

        Post post = isPresentPost(editRequest.getPostId());
        isWriter(post, user.getId());
        checkImageNumber(fileUploadRequests.size());
        List<String> editImageUrls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(editRequest.getImageUrls())) {
            editImageUrls.addAll(editRequest.getImageUrls());
        }
        if (!CollectionUtils.isEmpty(fileUploadRequests)) {
            for (FileUploadRequest fileUploadRequest : fileUploadRequests) {
                editImageUrls.add(fileService.upload(fileUploadRequest, POST_IMAGE_DIRECTORY));
            }
        }
        checkImageNumber(editImageUrls.size());

        post.editPost(editRequest.getTopicId(), editRequest.getContent(), editImageUrls);
        postRepository.save(post);
        return PostResponse.builder()
                           .post(PostDto.fromEntity(post))
                           .build();
    }

    // 게시물 삭제
    public void deletePost(PostDeleteRequest deleteRequest, User user) {
        Post post = isPresentPost(deleteRequest.getPostId());
        isWriter(post, user.getId());
        postRepository.delete(post);
    }

    private void validateUser(Long userId, User user) {
        if (user == null || !userId.equals(user.getId())) {
            throw new IllegalArgumentException("User 정보가 일치하지 않습니다.");
        }
    }

    private Post isPresentPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new IllegalArgumentException("해당 postID에 해당하는 게시물은 존재하지 않습니다. userID: " + postId));
    }

    private void isWriter(Post post, Long userId) {
        if (!post.getCreatedBy().equals(userId)) {
            throw new IllegalArgumentException("userId에 해당하는 사용자가 작성한 게시물이 아닙니다. userID: " + userId);
        }
    }

    private void checkImageNumber(int size) {
        if (size > 5) {
            throw new IllegalArgumentException("게시물에 업로드할 수 있는 이미지의 개수는 최대 5개 입니다.");
        }
    }
}
