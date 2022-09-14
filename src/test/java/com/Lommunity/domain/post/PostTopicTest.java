package com.Lommunity.domain.post;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTopicTest {

    @Test
    public void postTopicFindTest() {
        PostTopic topicById1 = PostTopic.findTopicById(1L);
        PostTopic topicById2 = PostTopic.findTopicById(3L);
        System.out.println(topicById1.name());
        assertThat(topicById1.name()).isEqualTo("QUESTION");
        assertThat(topicById2.getDescription()).isEqualTo("동네 소식");
    }
}