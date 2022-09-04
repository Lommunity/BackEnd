package com.Lommunity.application.post.dto;

import com.Lommunity.domain.post.PostTopic;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTopicDto {
    private Long id;
    private String description;

    public static PostTopicDto fromPostTopic(PostTopic postTopic) {
        return PostTopicDto.builder()
                           .id(postTopic.getId())
                           .description(postTopic.getDescription())
                           .build();
    }
}
