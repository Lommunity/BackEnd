package com.Lommunity.application.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentEditRequest {

    private String content;
}
