package com.Lommunity.application.post.dto.request;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private Long userId;
    private Long topicId;
    private String content;
}
