package com.Lommunity.application.user;

import com.Lommunity.application.user.dto.JoinRequest;
import com.Lommunity.application.user.dto.JoinResponse;
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
    public JoinResponse join(JoinRequest joinRequest) {
        User user = userRepository.findById(joinRequest.getUserId())
                                  .orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자가 존재하지 않습니다. userID: " + joinRequest.getUserId()));
        if (user.isRegistered()) {
            throw new IllegalArgumentException("해당 사용자는 이미 가입되어 있습니다.");
        }
        user.userJoin(joinRequest);
        userRepository.save(user);
        return JoinResponse.builder()
                           .user(UserDto.fromEntity(user))
                           .build();
    }

}
