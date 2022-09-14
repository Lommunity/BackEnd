package com.Lommunity.application.post.dto.response;

import com.Lommunity.application.post.dto.PostTopicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostTopicListResponse {
    private List<PostTopicDto> topics;
}
