package com.example.snowman.service;

import com.example.snowman.entity.User;
import com.example.snowman.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestLoginService {

    private final UserRepository userRepository; // 너 프로젝트 repo로 교체

    private final HttpSessionSecurityContextRepository securityContextRepo =
            new HttpSessionSecurityContextRepository();

    public void login(HttpServletRequest request, HttpServletResponse response) {

        // ✅ 1) 테스트 sub 하나 고정
        String sub = "100020104821018835856";

        // ✅ 2) DB에 User 없으면 하나 만들어 저장 (googleSub 컬럼에 sub 저장)
        userRepository.findByGoogleSub(sub)
                .orElseGet(() -> userRepository.save(User.create(sub)));

        // ✅ 3) OAuth2User principal 생성 (sub 포함)
        Map<String, Object> attrs = Map.of("sub", sub);
        OAuth2User oAuth2User = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                attrs,
                "sub"
        );

        // ✅ 4) Authentication 생성 + 세션 저장
        var auth = new UsernamePasswordAuthenticationToken(
                oAuth2User, null, oAuth2User.getAuthorities()
        );

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        securityContextRepo.saveContext(context, request, response);
    }
}
