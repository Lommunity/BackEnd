package com.Lommunity.application.post.dto;

import com.Lommunity.domain.post.PostTopic;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class PostTopicDto {
    private Long topicId;
    private String description;

    public static PostTopicDto fromPostTopic(PostTopic postTopic) {
        return PostTopicDto.builder()
                           .topicId(postTopic.getTopicId())
                           .description(postTopic.getDescription())
                           .build();
    }
}
