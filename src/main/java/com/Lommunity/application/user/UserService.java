package com.Lommunity.application.user;

import com.Lommunity.application.user.dto.JoinRequest;
import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    public User join(JoinRequest joinRequest) {
        User user = userRepository.findById(joinRequest.getId()).orElseThrow(() -> new IllegalArgumentException("userId에 해당하는 사용자가 존재하지 않습니다. userID: " + joinRequest.getId()));
        // findUser의 isRegistered를 검사 안해도 될까?
        user.userJoin(joinRequest);
        return user;
    }

    // 로그인
    public User login(String providerId) {
        return userRepository.findByProviderId(providerId)
                             .orElseThrow(() -> new IllegalArgumentException("providerId에 해당하는 사용자가 존재하지 않습니다. providerId: " + providerId));
    }
}
