package com.Lommunity.application.region;

import com.Lommunity.application.region.dto.RegionDto;
import com.Lommunity.domain.region.Region;
import com.Lommunity.domain.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionDto> findRegion(String word) {
        List<RegionDto> regionDtoList = new ArrayList<>();
        for (Region region : regionRepository.findAll()) {
            if (region.getFullName().contains(word)) {
                regionDtoList.add(RegionDto.fromEntity(region));
            }
        }
        return regionDtoList;
    }
}
