package com.Lommunity.domain.post;

import com.Lommunity.domain.common.ListStringConverter;
import com.Lommunity.domain.entity.BaseEntity;
import com.Lommunity.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
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
    private List<String> postImageUrls;

    @Builder
    public Post(User user, Long topicId, String content, List<String> postImageUrls) {
        if (topicId == null) throw new IllegalArgumentException("topicId가 비어있으면 안됩니다.");
        if (StringUtils.isEmpty(content)) throw new IllegalArgumentException("빈 내용으로 게시물 등록은 불가능합니다.");
        this.user = user;
        this.topicId = topicId;
        this.content = content;
        this.postImageUrls = postImageUrls;
    }

    public void editPost(Long topicId, String content, List<String> postImageUrls) {
        if (topicId == null) throw new IllegalArgumentException("topicId는 비어있으면 안됩니다.");
        if (StringUtils.isEmpty(content)) throw new IllegalArgumentException("빈 내용으로 게시물 수정이 불가능합니다.");
        PostTopic.isPresentTopicId(topicId);

        this.topicId = topicId;
        this.content = content;
        this.postImageUrls = postImageUrls;
    }
}
