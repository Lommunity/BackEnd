package com.Lommunity.domain.post;

import com.Lommunity.domain.converter.ListStringConverter;
import com.Lommunity.domain.entity.BaseEntity;
import com.Lommunity.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "posts")
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Long topicId;
    private String content;

    @Convert(converter = ListStringConverter.class)
    @Column(length = 500)
    private List<String> imageUrls;

    public void editPost(Long userId, Long topicId, String content) {
        PostTopic.isPresentTopicId(topicId);

        if (topicId != null) {
            this.topicId = topicId;
        }

        if (content != null) {
            this.content = content;
        }
    }
}
