package com.Lommunity.application.post.dto.response;

import com.Lommunity.application.post.dto.PostDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class PostPageResponse {
    private Page<PostDto> postPage;
}
