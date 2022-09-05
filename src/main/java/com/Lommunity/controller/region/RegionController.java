package com.Lommunity.controller.region;

import com.Lommunity.application.region.RegionService;
import com.Lommunity.application.region.dto.RegionSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/search")
    public RegionSearchResponse searchRegion(@RequestParam("query") String query) {
        // query → 검색어
        return RegionSearchResponse.builder()
                                   .regions(regionService.findRegion(query))
                                   .build();
    }
}
