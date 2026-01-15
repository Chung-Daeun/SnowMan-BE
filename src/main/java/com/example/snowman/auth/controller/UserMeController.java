package com.example.snowman.auth.controller;

import com.example.snowman.common.ApiResponse;
import com.example.snowman.common.resolver.CurrentUser;
import com.example.snowman.entity.User;
import com.example.snowman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserMeController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ApiResponse<MeResponse> me(@CurrentUser User user) {
        return ApiResponse.of(new MeResponse(
            user.getUserId(),
            user.getGoogleSub(),
            user.getNickname(),
            user.getBirthDate() != null ? user.getBirthDate().toString() : null
        ));
    }

    @PutMapping("/me")
    public ApiResponse<MeResponse> updateProfile(
        @CurrentUser User user,
        @RequestBody UpdateProfileRequest request
    ) {
        user.updateProfile(request.nickname(), request.birthdate() != null ? LocalDate.parse(request.birthdate()) : null);
        User updatedUser = userRepository.save(user);
        
        return ApiResponse.of(new MeResponse(
            updatedUser.getUserId(),
            updatedUser.getGoogleSub(),
            updatedUser.getNickname(),
            updatedUser.getBirthDate() != null ? updatedUser.getBirthDate().toString() : null
        ));
    }

    public record MeResponse(
        Long userId,
        String googleSub,
        String nickname,
        String birthdate
    ) {}

    public record UpdateProfileRequest(
        String nickname,
        String birthdate
    ) {}
}
