package com.Lommunity.application.post.dto;

import com.Lommunity.application.user.dto.UserDto;
import com.Lommunity.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private UserDto user;
    private Long topicId;
    private String content;
    private String imageUrl;
    private Long createdBy;
    private Long lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                      .postId(post.getId())
                      .topicId(post.getTopicId())
                      .user(UserDto.fromEntity(post.getUser()))
                      .content(post.getContent())
                      .imageUrl(post.getImageUrl())
                      .createdBy(post.getCreatedBy())
                      .lastModifiedBy(post.getLastModifiedBy())
                      .createdDate(post.getCreatedDate())
                      .lastModifiedDate(post.getLastModifiedDate())
                      .build();
    }
}
