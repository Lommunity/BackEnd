package com.Lommunity.application.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostEditRequest {

    private Long userId;
    private Long postId;
    private String title;
    private String content;
    private String imageUrl;
}
