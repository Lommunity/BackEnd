package com.Lommunity.application.comment.dto.request;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    private Long postId;
    private String content;
}
