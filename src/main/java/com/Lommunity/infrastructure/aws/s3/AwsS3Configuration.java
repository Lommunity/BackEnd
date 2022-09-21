package com.Lommunity.infrastructure.aws.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Configuration {
    @Bean
    public S3Client s3Client(AwsS3Properties s3Properties) {
        AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(s3Properties.getAccessKeyId(),
                s3Properties.getSecretAccessKey());
        return S3Client.builder()
                       .region(Region.AP_NORTHEAST_2)
                       .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
                       .build();
    }
}
