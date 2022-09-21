package com.Lommunity.infrastructure.aws.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3Properties {
    private String bucket;
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
}
