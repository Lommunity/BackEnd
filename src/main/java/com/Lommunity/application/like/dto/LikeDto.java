package com.Lommunity.application.like.dto;

import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.user.dto.UserDto;
import com.Lommunity.domain.like.Like;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LikeDto {
    private Long likeId;
    private UserDto user;
    private PostDto post;

    public static LikeDto fromEntity(Like like, Long commentCount) {
        return LikeDto.builder()
                      .likeId(like.getId())
                      .user(UserDto.fromEntity(like.getUser()))
                      .post(PostDto.fromEntityWithCommentCount(like.getPost(), commentCount))
                      .build();
    }
}
