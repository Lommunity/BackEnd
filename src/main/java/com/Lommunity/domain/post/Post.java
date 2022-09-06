package com.Lommunity.domain.post;

import com.Lommunity.domain.entity.BaseTimeEntity;
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
@Entity(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Long topicId;
    private String content;
    private String imageUrl;
    private Long createdBy;
    private Long lastModifiedBy;

    public void editPost(Long userId, Long topicId, String content, String imageUrl) {
        PostTopic.isPresentTopicId(topicId);
        this.lastModifiedBy = userId;

        if (topicId != null) {
            this.topicId = topicId;
        }

        if (content != null) {
            this.content = content;
        }
        this.imageUrl = imageUrl;
    }

}
