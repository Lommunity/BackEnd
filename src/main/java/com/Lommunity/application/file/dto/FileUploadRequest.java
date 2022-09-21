package com.Lommunity.application.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {
    private String contentType;
    private String filename;
    private byte[] bytes;
    private long size;
}
