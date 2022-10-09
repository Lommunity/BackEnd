package com.Lommunity.controller.Like;

import com.Lommunity.application.like.LikeService;
import com.Lommunity.application.like.dto.response.LikeResponse;
import com.Lommunity.domain.user.User;
import com.Lommunity.infrastructure.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public LikeResponse createLike(@PathVariable("postId") Long postId, @AuthUser User user) {
        return likeService.createLike(postId, user);
    }

    @DeleteMapping("/{postId}")
    public void deleteLike(@PathVariable("postId") Long postId, @AuthUser User user) {
        likeService.deleteLike(postId, user);
    }
}
