package com.Lommunity.application.comment.dto.response;

import com.Lommunity.application.comment.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
    CommentDto comment;
}
