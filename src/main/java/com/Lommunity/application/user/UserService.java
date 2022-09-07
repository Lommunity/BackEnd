package com.Lommunity.application.user;

import com.Lommunity.application.user.dto.RegisterRequest;
import com.Lommunity.application.user.dto.RegisterResponse;
import com.Lommunity.application.user.dto.UserDto;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = userRepository.findById(registerRequest.getId())
                                  .orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자가 존재하지 않습니다. userID: " + registerRequest.getId()));
        if (user.isRegistered()) {
            throw new IllegalArgumentException("해당 사용자는 이미 가입되어 있습니다.");
        }
        user.registerInfo(registerRequest);
        return RegisterResponse.builder()
                               .user(UserDto.fromEntity(user))
                               .build();
    }

}
