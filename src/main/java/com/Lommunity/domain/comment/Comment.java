package com.Lommunity.domain.comment;

import com.Lommunity.domain.entity.BaseTimeEntity;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    public void editComment(String content) {
        if (content == null) {
            throw new IllegalArgumentException("빈 내용으로 코멘트 수정이 불가능합니다.");
        }
        this.content = content;
    }
}