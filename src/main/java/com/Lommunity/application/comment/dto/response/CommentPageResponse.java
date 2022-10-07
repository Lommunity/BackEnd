package com.Lommunity.application.comment.dto.response;

import com.Lommunity.application.comment.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class CommentPageResponse {
    private Page<CommentDto> commentPage;
}
