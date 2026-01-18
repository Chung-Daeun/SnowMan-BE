package com.example.snowman.controller;

import com.example.snowman.common.resolver.CurrentUser;
import com.example.snowman.entity.User;
import com.example.snowman.repository.UserRepository;
import com.example.snowman.service.TestLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserMeController {
    private final UserRepository userRepository;
    private final TestLoginService testLoginService;

    @GetMapping("/me")
    public MeResponse me(@CurrentUser User user) {
        return new MeResponse(user.getUserId(), user.getNickname(), user.getBirthDate());
    }

    @PostMapping("/test-login")
    public void testLogin(HttpServletRequest request, HttpServletResponse response) {
        testLoginService.login(request, response);
    }

    @PostMapping("/me/update")
    public MeResponse updateProfile(@CurrentUser User user, @RequestBody UpdateProfileRequest request) {
        user.updateProfile(request.nickname(), request.birthDate());
        userRepository.save(user); // 변경사항 저장
        return new MeResponse(user.getUserId(), user.getNickname(), user.getBirthDate());
    }


    public record MeResponse(Long userId, String nickname, LocalDate birthDate) {}

    public record UpdateProfileRequest(String nickname, String birthDate) {}
}
