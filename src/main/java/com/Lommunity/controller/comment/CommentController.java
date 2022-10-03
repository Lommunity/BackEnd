package com.Lommunity.controller.comment;

import com.Lommunity.application.comment.CommentService;
import com.Lommunity.application.comment.dto.request.CommentCreateRequest;
import com.Lommunity.application.comment.dto.request.CommentEditRequest;
import com.Lommunity.application.comment.dto.response.CommentResponse;
import com.Lommunity.domain.user.User;
import com.Lommunity.infrastructure.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponse createComment(@RequestBody CommentCreateRequest createRequest, @AuthUser User user) {
        return commentService.createComment(createRequest, user);
    }

    @PutMapping("/{commentId}")
    public CommentResponse editComment(@PathVariable("commentId") Long commentId,
                                       @RequestBody CommentEditRequest editRequest,
                                       @AuthUser User user) {
        return commentService.editComment(commentId, editRequest, user);
    }
}
