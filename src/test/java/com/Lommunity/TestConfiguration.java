package com.Lommunity;

import com.Lommunity.application.file.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public FileService fileService() {
        return new FakeFileService();
    }
}
