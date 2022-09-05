package com.Lommunity.application.region;

import com.Lommunity.application.region.dto.RegionDto;
import com.Lommunity.domain.region.RegionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegionServiceTest {

    @Autowired
    RegionService regionService;
    @Autowired
    RegionRepository regionRepository;

    @Test
    public void regionSearchTest() {
        List<RegionDto> searchRegion = regionService.findRegion("연제");
        for (RegionDto regionDto : searchRegion) {
            Assertions.assertThat(regionDto.getFullName()).contains("연제");
        }
    }

}