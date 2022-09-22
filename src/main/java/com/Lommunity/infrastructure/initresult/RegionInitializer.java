package com.Lommunity.infrastructure.initresult;

import com.Lommunity.domain.region.Region;
import com.Lommunity.domain.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RegionInitializer implements ApplicationRunner {

    private final RegionRepository regionRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("data/result.csv");
        try (InputStream inputStream = classPathResource.getInputStream()) {
            List<Region> regions = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .skip(1)
                    .map(line -> {
                        String[] split = line.split(",");
                        Long parentCode = Objects.equals(split[2], "") ? null : Long.parseLong(split[2]);

                        return Region.builder()
                                     .code(Long.parseLong(split[1]))
                                     .level(Long.parseLong(split[0]))
                                     .parentCode(parentCode)
                                     .fullname(split[3])
                                     .build();
                    })
                    .collect(Collectors.toList());
            regionRepository.saveAll(regions);
        }

    }
}
