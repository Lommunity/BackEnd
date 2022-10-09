package com.Lommunity.application.like.dto.response;

import com.Lommunity.application.like.dto.LikeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    private LikeDto like;
}
