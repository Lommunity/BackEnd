package com.Lommunity.application.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEditRequest {

    private Long postId;
    private Long topicId;
    private String content;
    private List<String> postImageUrls;

    public void nullImageUrls(List<String> imageUrls) {
        this.postImageUrls = imageUrls;
    }
}
