package com.Lommunity.application.region.dto;

import com.Lommunity.domain.region.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDto {

    private Long code;
    private Long parentCode;
    private Long level;
    private String fullName;

    public static RegionDto fromEntity(Region region) {
        return RegionDto.builder()
                        .code(region.getCode())
                        .parentCode(region.getParentCode())
                        .level(region.getLevel())
                        .fullName(region.getFullname())
                        .build();
    }
}
