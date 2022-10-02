package com.Lommunity.infrastructure.aws.s3;

import com.Lommunity.application.file.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Profile("!test")
@Configuration
public class AwsS3Configuration {

    @Bean
    public AwsS3Properties s3Properties() {
        return new AwsS3Properties();
    }
    @Bean
    public S3Client s3Client(AwsS3Properties s3Properties) {
        AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(s3Properties.getAccessKeyId(),
                s3Properties.getSecretAccessKey());
        return S3Client.builder()
                       .region(Region.AP_NORTHEAST_2)
                       .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
                       .build();
    }

    @Bean
    public FileService fileService(S3Client s3Client, AwsS3Properties s3Properties) {
        return new AwsS3FileService(s3Client, s3Properties);
    }
}
