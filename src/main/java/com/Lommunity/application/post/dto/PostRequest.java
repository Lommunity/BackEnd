package com.Lommunity.application.post.dto;

import lombok.*;

import static com.Lommunity.domain.post.Post.PostTopic;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private Long userId;
//    private PostTopic topic;
    private String title;
    private String content;
    private String imageUrl;
}
