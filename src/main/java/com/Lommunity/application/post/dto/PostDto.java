package com.Lommunity.application.post.dto;

import com.Lommunity.application.user.dto.UserDto;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.post.PostTopic;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PostDto {
    private Long postId;
    private UserDto writer;
    private PostTopicDto topic;
    private String content;
    private Long commentCount;
    private Long likeCount;
    private boolean userLike;
    private List<String> postImageUrls;
    private Long secondRegionLevel;
    private Long thirdRegionLevel;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static PostDto fromEntityWithCommentCount(Post post, Long commentCount, Long likeCount, boolean writerLike) {
        return PostDto.builder()
                      .postId(post.getId())
                      .topic(PostTopicDto.fromPostTopic(PostTopic.findTopicById(post.getTopicId())))
                      .writer(UserDto.fromEntity(post.getUser()))
                      .content(post.getContent())
                      .commentCount(commentCount)
                      .likeCount(likeCount)
                      .userLike(writerLike)
                      .postImageUrls(post.getPostImageUrls())
                      .secondRegionLevel(post.getSecondLevelRegionCode())
                      .thirdRegionLevel(post.getThirdLevelRegionCode())
                      .createdDate(post.getCreatedDate())
                      .lastModifiedDate(post.getLastModifiedDate())
                      .build();
    }
}
