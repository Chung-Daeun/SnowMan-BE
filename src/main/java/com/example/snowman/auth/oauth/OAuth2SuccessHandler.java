package com.example.snowman.auth.oauth;

import com.example.snowman.entity.User;
import com.example.snowman.global.config.AppProperties;
import com.example.snowman.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.snowman.auth.oauth.RedirectAwareAuthorizationRequestResolver.SESSION_REDIRECT_URI_KEY;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final AppProperties appProperties;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String googleSub = String.valueOf(oAuth2User.getAttributes().get("sub"));

        userRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> userRepository.save(User.create(googleSub)));

        // 세션 확정
        HttpSession session = request.getSession(true);

        // 로그인 시작 시 저장해둔 redirect_uri 사용
        String redirectUri = (String) session.getAttribute(SESSION_REDIRECT_URI_KEY);
        session.removeAttribute(SESSION_REDIRECT_URI_KEY); // 한 번 쓰고 제거

        // fallback은 프로퍼티 기본값
        String target = (redirectUri != null && !redirectUri.isBlank())
                ? redirectUri
                : appProperties.getOauth2SuccessRedirectUrl();

        redirectStrategy.sendRedirect(request, response, target);
    }

    public record LoginResponse(Long userId, String googleSub) {}
}
