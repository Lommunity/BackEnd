package com.Lommunity.application;

import com.Lommunity.domain.user.User;
import com.Lommunity.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 로그인
    public User login(String providerId) {
        return userRepository.findByProviderId(providerId)
                             .orElseThrow(() -> new IllegalArgumentException("providerId에 해당하는 사용자가 존재하지 않습니다. providerId: " + providerId));
    }

    public boolean isRegistered(String providerId) {
        Optional<User> byProviderId = userRepository.findByProviderId(providerId);
        if (byProviderId.isPresent()) {
            return byProviderId.get().isRegistered();
        } else {
            throw new IllegalArgumentException("providerId에 해당하는 사용자가 존재하지 않습니다. providerId: " + providerId);
        }
    }
}
