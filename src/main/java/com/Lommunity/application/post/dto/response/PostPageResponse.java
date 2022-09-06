package com.Lommunity.application.post.dto.response;

import com.Lommunity.application.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostPageResponse {
    private Page<PostDto> postDtoPage;
}
