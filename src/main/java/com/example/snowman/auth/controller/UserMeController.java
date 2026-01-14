package com.example.snowman.auth.controller;

import com.example.snowman.common.resolver.CurrentUser;
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

    @GetMapping("/me")
    public MeResponse me(@CurrentUser User user) {
        return new MeResponse(user.getUserId(), user.getGoogleSub());
    }

    public record MeResponse(Long userId, String googleSub) {}
}
