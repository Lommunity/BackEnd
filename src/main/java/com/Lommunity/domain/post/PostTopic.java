package com.Lommunity.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PostTopic {
    QUESTION(1L, "동네 질문"),
    RESTAURANT(2L, "동네 맛집"),
    NEWS(3L, "동네 소식"),
    HOBBY(4L, "취미생활"),
    LOST(5L, "분실/실종"),
    PLEASE(6L, "부탁해요"),
    DAILY(7L, "일상"),
    ;

    private long id;
    private String description;

    private static Map<Long, PostTopic> CACHED_MAP = Arrays.stream(PostTopic.values())
                                                           .collect(Collectors.toMap(value -> value.getId(), value -> value));

    public static PostTopic findTopicById(Long id) {
        isPresentTopicId(id);
        return CACHED_MAP.get(id);
    }

    public static void isPresentTopicId(Long id) {

        if (!Arrays.stream(PostTopic.values())
                   .collect(Collectors.toMap(value -> value.getId(), value -> value)).containsKey(id)) {
            throw new IllegalArgumentException("ID에 해당하는 postTopic은 존재하지 않습니다. ID: " + id);
        }
    }

}
