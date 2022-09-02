package com.Lommunity.application.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static com.Lommunity.domain.post.Post.PostTopic;

@Getter
@ToString
@Builder
public class PostRequest {

    private Long userId;
//    private PostTopic topic;
    private String title;
    private String content;
    private String imageUrl;
}
