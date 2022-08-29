package com.Lommunity.application;

import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입 → User.registered가 false이면 DB에 유저 정보는 있지만, 회원가입을 완료하지는 않은 상태이다.
    public User join(User user) {
        if (user.isRegistered()) {
            throw new IllegalArgumentException("이미 회원가입이 되어 있습니다.");
        }
        User savedUser = userRepository.save(user);
        savedUser.register(); // registered false 에서 true로 변경
        return savedUser;
    }

    // 로그인
    public User login(String providerId) {
        return userRepository.findByProviderId(providerId)
                             .orElseThrow(() -> new IllegalArgumentException("providerId에 해당하는 사용자가 존재하지 않습니다. providerId: " + providerId));
    }
}
