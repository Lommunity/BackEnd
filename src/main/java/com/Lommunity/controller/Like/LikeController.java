package com.Lommunity.controller.Like;

import com.Lommunity.application.like.LikeService;
import com.Lommunity.application.like.dto.request.LikeRequest;
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
    public void createLike(@RequestBody LikeRequest likeRequest, @AuthUser User user) {
        likeService.createLike(likeRequest, user);
    }

    @DeleteMapping()
    public void deleteLike(@RequestBody LikeRequest likeRequest, @AuthUser User user) {
        likeService.deleteLike(likeRequest, user);
    }
}
