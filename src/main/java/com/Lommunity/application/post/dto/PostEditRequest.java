package com.Lommunity.application.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEditRequest {

    private Long userId;
    private Long postId;
    private Long topicId;
    private String content;
    private String imageUrl;
}
