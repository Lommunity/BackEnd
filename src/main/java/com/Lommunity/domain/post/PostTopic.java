package com.Lommunity.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

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

    public static PostTopic findTopicById(Long id) {
        isPresentTopicId(id);
        return mapInit().get(id);
    }

    public static Map<Long, PostTopic> mapInit() {
        Map<Long, PostTopic> longToPostTopicMap = new HashMap<>();
        for (PostTopic value : PostTopic.values()) {
            longToPostTopicMap.put(value.getId(), value);
        }
        return longToPostTopicMap;
    }

    public static void isPresentTopicId(Long id) {
        if (!PostTopic.mapInit().containsKey(id)) {
            throw new IllegalArgumentException("ID에 해당하는 postTopic은 존재하지 않습니다. ID: " + id);
        }
    }

}
