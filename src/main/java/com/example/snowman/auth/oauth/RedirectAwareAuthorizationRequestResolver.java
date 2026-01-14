package com.example.snowman.auth.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectAwareAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    public static final String SESSION_REDIRECT_URI_KEY = "OAUTH2_REDIRECT_URI";

    private final RedirectUriValidator redirectUriValidator;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequestResolver delegate =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request);
        storeRedirectUriIfPresent(request);
        return authorizationRequest;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequestResolver delegate =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request, clientRegistrationId);
        storeRedirectUriIfPresent(request);
        return authorizationRequest;
    }

    private void storeRedirectUriIfPresent(HttpServletRequest request) {
        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri == null || redirectUri.isBlank()) return;

        if (!redirectUriValidator.isAllowed(redirectUri)) {
            // 허용되지 않으면 저장하지 않음 (오픈 리다이렉트 방지)
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_REDIRECT_URI_KEY, redirectUri);
    }
}
