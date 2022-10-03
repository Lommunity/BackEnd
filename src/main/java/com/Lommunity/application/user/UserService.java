package com.Lommunity.application.user;

import com.Lommunity.application.file.FileService;
import com.Lommunity.application.file.dto.FileUploadRequest;
import com.Lommunity.application.user.dto.*;
import com.Lommunity.application.user.dto.request.RegisterRequest;
import com.Lommunity.application.user.dto.request.UserEditRequest;
import com.Lommunity.application.user.dto.response.RegisterResponse;
import com.Lommunity.application.user.dto.response.UserEditResponse;
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
        User user = findUser(registerRequest.getUserId());

        if (user.isRegistered()) {
            throw new IllegalArgumentException("해당 사용자는 이미 가입되어 있습니다.");
        }

        if (registerRequest.getRegionCode() == null) {
            throw new IllegalArgumentException("회원가입 시 지역 선택은 필수입니다.");
        }
        Region region = findRegion(registerRequest.getRegionCode());

        String profileImageUrl = null;
        if (fileUploadRequest != null) {
            profileImageUrl = uploadProfileImage(fileUploadRequest);
        }

        user.registerInfo(registerRequest, profileImageUrl, region);
        return RegisterResponse.builder()
                               .user(UserDto.fromEntity(user))
                               .build();
    }

    public UserEditResponse edit(Long userId, UserEditRequest editRequest, FileUploadRequest fileUploadRequest) {
        User user = findUser(userId);

        String nickname = editRequest.getNickname();

        Region region = null;
        if (editRequest.getRegionCode() != null) {
            region = findRegion(editRequest.getRegionCode());
        }

        String profileImageUrl;
        if (fileUploadRequest != null) {
            profileImageUrl = uploadProfileImage(fileUploadRequest);
        } else {
            profileImageUrl = editRequest.getProfileImageUrl();
        }

        user.editUserInfo(nickname, profileImageUrl, region);

        return UserEditResponse.builder()
                               .user(UserDto.fromEntity(user))
                               .build();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자가 존재하지 않습니다. userID: " + userId));
    }

    private Region findRegion(Long regionCode) {
        return regionRepository.findById(regionCode)
                               .orElseThrow(() -> new IllegalArgumentException("regionCode에 해당하는 Region이 없습니다. regionCode: " + regionCode));
    }

    private String uploadProfileImage(FileUploadRequest uploadRequest) {
        return fileService.upload(uploadRequest, PROFILE_IMAGE_DIRECTORY);
    }

}
