package com.Lommunity.infrastructure.initresult;

import com.Lommunity.domain.region.Region;
import com.Lommunity.domain.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
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
        List<String> lines = Files.readAllLines(Paths.get(classPathResource.getURI()));
        List<Region> regions = lines.stream()
                                    .skip(1)
                                    .map(line -> {
                                        String[] split = line.split(",");
                                        Long parentCode = Objects.equals(split[2], "") ? null : Long.parseLong(split[2]);

                                        return Region.builder()
                                                     .code(Long.parseLong(split[1]))
                                                     .level(Long.parseLong(split[0]))
                                                     .parentCode(parentCode)
                                                     .fullName(split[3])
                                                     .build();
                                    })
                                    .collect(Collectors.toList());
        regionRepository.saveAll(regions);
    }
}
