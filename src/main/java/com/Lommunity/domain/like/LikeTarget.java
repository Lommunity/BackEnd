package com.Lommunity.domain.like;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LikeTarget {
    POST;

    @JsonCreator
    public static LikeTarget from(String s) {
        return LikeTarget.valueOf(s.toUpperCase());
    }
}
