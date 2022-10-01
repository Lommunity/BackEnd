package com.Lommunity;

import com.Lommunity.application.file.FileService;
import com.Lommunity.application.file.dto.FileUploadRequest;

public class FakeFileService implements FileService {

    @Override
    public String upload(FileUploadRequest uploadRequest, String directory) {
        return uploadRequest.getFilename();
    }
}
