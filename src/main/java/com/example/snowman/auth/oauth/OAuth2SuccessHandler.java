package com.example.snowman.auth.oauth;

import com.example.snowman.entity.User;
import com.example.snowman.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String googleSub = String.valueOf(oAuth2User.getAttributes().get("sub"));

        User user = userRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> userRepository.save(User.create(googleSub)));

        // 세션 ID 포함
        String sessionId = request.getSession(true).getId();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), Map.of(
                "userId", user.getUserId(),
                "googleSub", user.getGoogleSub(),
                "sessionId", sessionId
        ));
    }

    public record LoginResponse(Long userId, String googleSub) {}
}
