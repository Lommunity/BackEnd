package com.Lommunity.domain.region;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class RegionRepositoryTest {

    @Autowired
    RegionRepository regionRepository;

    @Test
    public void finRegionListsBySearch() {
        List<Region> regionList = regionRepository.findRegionByWord("연제");
        for (Region region : regionList) {
            System.out.println(region.getFullname());
        }

    }
}