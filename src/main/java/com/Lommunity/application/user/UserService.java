package com.Lommunity.application.user;

import com.Lommunity.application.file.FileService;
import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.user.dto.*;
import com.Lommunity.domain.region.Region;
import com.Lommunity.domain.region.RegionRepository;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private static final String PROFILE_IMAGE_DIRECTORY = "profile";

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final FileService fileService;

    // 회원 가입
    public RegisterResponse register(RegisterRequest registerRequest, FileUploadRequest fileUploadRequest) {
        User user = getUser(registerRequest.getUserId());

        if (user.isRegistered()) {
            throw new IllegalArgumentException("해당 사용자는 이미 가입되어 있습니다.");
        }

        if (registerRequest.getRegionCode() == null) {
            throw new IllegalArgumentException("회원가입 시 지역 선택은 필수입니다.");
        }
        Region region = getRegion(registerRequest.getRegionCode());

        String profileImageUrl = null;
        if (fileUploadRequest != null) {
            profileImageUrl = getProfileImageUrl(fileUploadRequest, PROFILE_IMAGE_DIRECTORY);
        }

        user.registerInfo(registerRequest, profileImageUrl, region);
        return RegisterResponse.builder()
                               .user(UserDto.fromEntity(user))
                               .build();
    }

    public UserEditResponse edit(Long userId, UserEditRequest editRequest, FileUploadRequest fileUploadRequest) {
        User user = getUser(userId);

        String nickname = null;
        if (editRequest.getNickname() != null) {
            nickname = editRequest.getNickname();
        }

        Region region = null;
        if (editRequest.getRegionCode() != null) {
            region = getRegion(editRequest.getRegionCode());
        }

        String profileImageUrl;
        if (fileUploadRequest != null) {
            profileImageUrl = getProfileImageUrl(fileUploadRequest, PROFILE_IMAGE_DIRECTORY);
        } else {
            profileImageUrl = editRequest.getProfileImageUrl();
        }

        user.editUserInfo(nickname, profileImageUrl, region);

        return UserEditResponse.builder()
                               .user(UserDto.fromEntity(user))
                               .build();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자가 존재하지 않습니다. userID: " + userId));
    }

    private Region getRegion(Long regionCode) {
        return regionRepository.findById(regionCode)
                               .orElseThrow(() -> new IllegalArgumentException("regionCode에 해당하는 Region이 없습니다. regionCode: " + regionCode));
    }

    private String getProfileImageUrl(FileUploadRequest uploadRequest, String directory) {
        return fileService.upload(uploadRequest, directory);
    }

}
