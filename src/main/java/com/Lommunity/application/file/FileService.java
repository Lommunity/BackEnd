package com.Lommunity.application.file;

import com.Lommunity.application.file.dto.FileUploadRequest;

public interface FileService {
    String upload(FileUploadRequest uploadRequest, String directory);
}
