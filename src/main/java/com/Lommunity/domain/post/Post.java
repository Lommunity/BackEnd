package com.Lommunity.domain.post;

import com.Lommunity.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "posts")
public class Post {

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
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public void editPost(Long userId, Long topicId, String content, String imageUrl) {
        PostTopic.isPresentTopicId(topicId);
        this.lastModifiedBy = userId;
        this.lastModifiedDate = LocalDateTime.now();
        this.topicId = topicId;

        if (content != null) {
            this.content = content;
        }
        this.imageUrl = imageUrl;
    }

}
