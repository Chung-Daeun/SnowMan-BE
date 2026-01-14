package com.example.snowman.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        // Google의 경우 "sub"가 고유 식별자
        Map<String, Object> attributes = user.getAttributes();
        if (!attributes.containsKey("sub")) {
            throw new OAuth2AuthenticationException("구글 사용자 식별자(sub)를 찾을 수 없습니다.");
        }
        return user;
    }
}
