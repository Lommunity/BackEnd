package com.Lommunity.application.post;

import com.Lommunity.application.file.FileService;
import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.post.dto.request.PostCreateRequest;
import com.Lommunity.application.post.dto.request.PostEditRequest;
import com.Lommunity.application.post.dto.response.PostPageResponse;
import com.Lommunity.application.post.dto.response.PostResponse;
import com.Lommunity.domain.comment.CommentRepository;
import com.Lommunity.domain.like.LikeRepository;
import com.Lommunity.domain.like.LikeTarget;
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

import static com.Lommunity.utils.PageUtils.sortByLastModifiedDate;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private static final String POST_IMAGE_DIRECTORY = "post";
    private final PostRepository postRepository;
    private final FileService fileService;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    // 게시물 작성
    public PostResponse createPost(PostCreateRequest createRequest,
                                   List<FileUploadRequest> fileUploadRequests,
                                   User user) {

        List<String> postImageUrls = new ArrayList<>();
        checkPostImageSize(fileUploadRequests.size());
        for (FileUploadRequest fileUploadRequest : fileUploadRequests) {
            String imageUrl = fileService.upload(fileUploadRequest, POST_IMAGE_DIRECTORY);
            postImageUrls.add(imageUrl);
        }

        Post post = postRepository.save(Post.builder()
                                            .user(user)
                                            .topicId(createRequest.getTopicId())
                                            .content(createRequest.getContent())
                                            .postImageUrls(postImageUrls)
                                            .secondLevelRegionCode(user.getRegion().getParentCode())
                                            .thirdLevelRegionCode(user.getRegion().getCode())
                                            .build());
        return PostResponse.builder()
                           .post(PostDto.fromEntityWithCommentCount(post, 0L, 0L, false))
                           .build();
    }

    // 게시물 수정
    public PostResponse editPost(Long postId,
                                 PostEditRequest editRequest,
                                 List<FileUploadRequest> fileUploadRequests,
                                 User user) {

        Post post = findPost(postId);
        isWriter(post, user.getId());
        checkPostImageSize(fileUploadRequests.size() + editRequest.getPostImageUrls().size());
        List<String> postImageUrls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(editRequest.getPostImageUrls())) {
            postImageUrls.addAll(editRequest.getPostImageUrls());
        }
        if (!CollectionUtils.isEmpty(fileUploadRequests)) {
            for (FileUploadRequest fileUploadRequest : fileUploadRequests) {
                postImageUrls.add(fileService.upload(fileUploadRequest, POST_IMAGE_DIRECTORY));
            }
        }

        post.editPost(editRequest.getTopicId(), editRequest.getContent(), postImageUrls);
        return PostResponse.builder()
                           .post(findPostDtoWithCountAndIsLike(post, user))
                           .build();
    }

    // 게시물 삭제
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);
        isWriter(post, user.getId());
        postRepository.delete(post);
    }

    public PostPageResponse searchPost(String word, User user, Pageable pageable) {
        Long secondRegionLevel = user.getRegion().getParentCode();
        Page<PostDto> postPageBySearch = postRepository.findPostBySecondLevelRegionCodeAndWord(secondRegionLevel, word, sortByLastModifiedDate(pageable))
                                                       .map((p) -> findPostDtoWithCountAndIsLike(p, user));
        return PostPageResponse.builder()
                               .postPage(postPageBySearch)
                               .build();
    }

    // 단일 게시물 조회
    public PostResponse getPost(Long postId, User user) {
        Post post = findPost(postId);
        return PostResponse.builder()
                           .post(findPostDtoWithCountAndIsLike(post, user))
                           .build();
    }

    // 전체 게시물 목록 조회
    public PostPageResponse getAllPostPage(User user, Pageable pageable) {
        Long secondRegionLevel = user.getRegion().getParentCode();
        Page<PostDto> postDtoPage = postRepository.findPostAllPageBySecondLevelRegionCode(secondRegionLevel, sortByLastModifiedDate(pageable))
                                                  .map((p) -> findPostDtoWithCountAndIsLike(p, user));
        return PostPageResponse.builder()
                               .postPage(postDtoPage)
                               .build();

    }

    // 작성자별 게시물 목록 조회 → Pagination
    public PostPageResponse getPostPageByUserId(Long userId, User user, Pageable pageable) {
        Page<PostDto> postDtoPage = postRepository.findPostPageByUserId(userId, sortByLastModifiedDate(pageable))
                                                  .map((p) -> findPostDtoWithCountAndIsLike(p, user));
        return PostPageResponse.builder()
                               .postPage(postDtoPage)
                               .build();
    }

    public PostPageResponse getPostPageByTopicId(Long topicId, User user, Pageable pageable) {
        Long secondRegionLevel = user.getRegion().getParentCode();
        Page<PostDto> postDtoPage = postRepository.findPostPageBySecondLevelRegionCodeAndTopicId(secondRegionLevel, topicId, sortByLastModifiedDate(pageable))
                                                  .map((p) -> findPostDtoWithCountAndIsLike(p, user));
        return PostPageResponse.builder()
                               .postPage(postDtoPage)
                               .build();
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new IllegalArgumentException("해당 postID에 해당하는 게시물은 존재하지 않습니다. userID: " + postId));
    }

    private void isWriter(Post post, Long userId) {
        if (!post.getCreatedBy().equals(userId)) {
            throw new IllegalArgumentException("userId에 해당하는 사용자가 작성한 게시물이 아닙니다. userID: " + userId);
        }
    }

    private void checkPostImageSize(int size) {
        if (size > 5) {
            throw new IllegalArgumentException("게시물에 업로드할 수 있는 이미지의 개수는 최대 5개 입니다.");
        }
    }

    private PostDto findPostDtoWithCountAndIsLike(Post post, User user) {
        return PostDto.fromEntityWithCommentCount(post,
                commentRepository.countByPostId(post.getId()),
                likeRepository.countByTargetTypeAndTargetId(LikeTarget.POST, post.getId()),
                likeRepository.existsByTargetTypeAndTargetIdAndUserId(LikeTarget.POST, post.getId(), user.getId()));
    }
}
