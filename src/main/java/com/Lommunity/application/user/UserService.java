package com.Lommunity.application.user;

import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.application.user.dto.RegisterResponse;
import com.Lommunity.application.user.dto.UserDto;
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

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    // 회원 가입
    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = userRepository.findById(registerRequest.getUserId())
                                  .orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자가 존재하지 않습니다. userID: " + registerRequest.getUserId()));

        if (user.isRegistered()) {
            throw new IllegalArgumentException("해당 사용자는 이미 가입되어 있습니다.");
        }
        Region region = regionRepository.findById(registerRequest.getRegionCode())
                                        .orElseThrow(() -> new IllegalArgumentException("regionCode에 해당하는 Region이 없습니다. regionCode: " + registerRequest.getRegionCode()));
        user.registerInfo(registerRequest, region);
        return RegisterResponse.builder()
                               .user(UserDto.fromEntity(user))
                               .build();
    }

}
