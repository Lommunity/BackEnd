package com.Lommunity.application.like.dto.request;

import com.Lommunity.domain.like.LikeTarget;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {
    private LikeTarget targetType;
    private Long targetId;
}
