package com.Lommunity.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {
    public static Pageable sortByLastModifiedDate(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("lastModifiedDate").descending());
    }
}
