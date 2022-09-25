package com.Lommunity.controller.user;

import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.application.user.dto.RegisterResponse;
import com.Lommunity.application.user.dto.UserDto;
import com.Lommunity.controller.user.dto.UserInfoResponse;
import com.Lommunity.domain.user.User;
import com.Lommunity.infrastructure.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping("/register")
    public RegisterResponse register(@RequestPart("dto") RegisterRequest request,
                                     @RequestPart(required = false) MultipartFile profileImageFile) {
        FileUploadRequest fileUploadRequest = null;
        if (profileImageFile != null) {
            ensureImageFile(profileImageFile);
            fileUploadRequest = toFileUploadRequest(profileImageFile);
        }
        return userService.register(request, fileUploadRequest);
    }

    @GetMapping("/retrieve")
    public UserInfoResponse getUserInfo(@AuthUser User user) {
        return UserInfoResponse.builder()
                               .user(UserDto.fromEntity(user))
                               .build();
    }

    private void ensureImageFile(MultipartFile profileImageFile) {
        if (!StringUtils.startsWith(profileImageFile.getContentType(), "image")) {
            throw new IllegalArgumentException("ContentType은 'image'로 시작해야합니다. ContentType: " + profileImageFile.getContentType());
        }
    }

    private FileUploadRequest toFileUploadRequest(MultipartFile multipartFile) {
        try {
            return FileUploadRequest.builder()
                                    .contentType(multipartFile.getContentType())
                                    .filename(multipartFile.getOriginalFilename())
                                    .bytes(multipartFile.getBytes())
                                    .size(multipartFile.getSize())
                                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException("유효하지 않은 multipartFile 입니다.", e);
        }
    }
}
