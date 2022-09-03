package com.Lommunity.application.post.dto;

import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.user.User;
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
    private User user;
    private String title;
    private String content;
    private String imageUrl;
    private Long createdBy;
    private Long lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                      .postId(post.getId())
                      .user(post.getUser())
                      .title(post.getTitle())
                      .content(post.getContent())
                      .imageUrl(post.getImageUrl())
                      .createdBy(post.getCreatedBy())
                      .lastModifiedBy(post.getLastModifiedBy())
                      .createdDate(post.getCreatedDate())
                      .lastModifiedDate(post.getLastModifiedDate())
                      .build();
    }
}
