package com.Lommunity.controller.Like;

import com.Lommunity.application.like.LikeService;
import com.Lommunity.domain.user.User;
import com.Lommunity.infrastructure.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping()
    public void createLike(@RequestParam("postId") Long postId, @AuthUser User user) {
        likeService.createLike(postId, user);
    }

    @DeleteMapping()
    public void deleteLike(@RequestParam("postId") Long postId, @AuthUser User user) {
        likeService.deleteLike(postId, user);
    }
}
