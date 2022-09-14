package com.Lommunity.application.post.dto.response;

import com.Lommunity.application.post.dto.PostDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostResponse {

    private PostDto post;
}
