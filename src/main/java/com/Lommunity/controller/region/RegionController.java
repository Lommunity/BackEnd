package com.Lommunity.controller.region;

import com.Lommunity.application.region.RegionService;
import com.Lommunity.application.region.dto.RegionDto;
import com.Lommunity.application.region.dto.RegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/search")
    public RegionResponse searchRegion(@RequestParam("query") String query) {
        // query → 검색어
        return RegionResponse.builder()
                             .searchRegions(regionService.findRegion(query))
                             .build();
    }
}
