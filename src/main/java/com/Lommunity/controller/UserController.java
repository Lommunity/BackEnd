package com.Lommunity.controller;

import com.Lommunity.application.user.UserDto;
import com.Lommunity.application.user.UserService;
import com.Lommunity.application.user.dto.JoinRequest;
import com.Lommunity.application.user.dto.JoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping("/register")
    public JoinResponse join(@RequestBody JoinRequest request) {
        return JoinResponse.builder()
                           .user(UserDto.fromEntity(userService.join(request)))
                           .build();
    }
}
