package com.example.snowman.auth.controller;

import com.example.snowman.entity.User;
import com.example.snowman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserMeController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new IllegalStateException("로그인되지 않은 사용자입니다.");
        }

        String googleSub = principal.getAttribute("sub");
        if (googleSub == null) {
            throw new IllegalStateException("구글 사용자 식별자(sub)를 찾을 수 없습니다.");
        }

        User user = userRepository.findByGoogleSub(googleSub)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        return new MeResponse(user.getUserId(), user.getGoogleSub());
    }

    public record MeResponse(Long userId, String googleSub) {}
}
