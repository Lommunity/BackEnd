package com.Lommunity.infrastructure.aws.s3;

import com.Lommunity.application.file.FileService;
import com.Lommunity.application.file.dto.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class AwsS3FileService implements FileService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");

    private final S3Client s3Client;
    private final AwsS3Properties s3Properties;

    @Override
    public String upload(FileUploadRequest uploadRequest, String directory) {
        if (StringUtils.isEmpty(uploadRequest.getContentType()) || StringUtils.isEmpty(directory)) {
            throw new IllegalArgumentException("ContentType과 direcotry는 비어있으면 안됩니다.");
        }
        String extension = FilenameUtils.getExtension(uploadRequest.getFilename());
        if (StringUtils.isEmpty(extension)) {
            throw new IllegalArgumentException("Extention은 비어있으면 안됩니다.");
        }
        String path = directory + "/" + createRandomFileName(extension); // directory + filename
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                            .bucket(s3Properties.getBucket())
                                                            .key(path)
                                                            .build();
        s3Client.putObject(putObjectRequest, toRequestBody(uploadRequest));
        return s3Properties.getEndpoint() + path;
    }

    private String createRandomFileName(String extension) {
        LocalDateTime now = LocalDateTime.now();
        return RandomStringUtils.randomAlphabetic(8) + "_" + now.format(formatter) + "." + extension;
    }

    private RequestBody toRequestBody(FileUploadRequest request) {
        return RequestBody.fromContentProvider(() -> new ByteArrayInputStream(request.getBytes()),
                request.getSize(),
                request.getContentType());
    }
}
