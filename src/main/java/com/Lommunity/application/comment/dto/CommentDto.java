package com.Lommunity.application.comment.dto;

import com.Lommunity.application.post.dto.PostDto;
import com.Lommunity.application.user.dto.UserDto;
import com.Lommunity.domain.comment.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CommentDto {
    private Long commentId;
    private PostDto post;
    private UserDto writer;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                         .commentId(comment.getId())
                         .post(PostDto.fromEntity(comment.getPost()))
                         .writer(UserDto.fromEntity(comment.getUser()))
                         .content(comment.getContent())
                         .createDate(comment.getCreatedDate())
                         .lastModifiedDate(comment.getLastModifiedDate())
                         .build();
    }
}
