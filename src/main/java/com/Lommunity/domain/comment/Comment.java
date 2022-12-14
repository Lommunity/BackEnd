package com.Lommunity.domain.comment;

import com.Lommunity.domain.entity.BaseTimeEntity;
import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
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

    @Builder
    public Comment(Post post, User user, String content) {
        validate(content);
        this.post = post;
        this.user = user;
        this.content = content;
    }

    public void editComment(String content) {
        validate(content);
        this.content = content;
    }

    private void validate(String content) {
        if (StringUtils.isEmpty(content)) throw new IllegalArgumentException("빈 내용으로 댓글 등록이 불가능합니다.");
    }
}
