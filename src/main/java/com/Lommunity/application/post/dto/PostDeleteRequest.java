package com.Lommunity.application.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDeleteRequest {
    private Long userId;
    private Long postId;
}
