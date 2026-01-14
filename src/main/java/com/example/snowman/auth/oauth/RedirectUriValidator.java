package com.example.snowman.auth.oauth;

import com.example.snowman.global.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class RedirectUriValidator {

    private final AppProperties appProperties;

    public boolean isAllowed(String redirectUri) {
        if (redirectUri == null || redirectUri.isBlank()) return false;

        URI uri;
        try {
            uri = URI.create(redirectUri);
        } catch (Exception e) {
            return false;
        }

        // scheme/host 필수 (상대경로 방지)
        if (uri.getScheme() == null || uri.getHost() == null) return false;

        // origin 추출: scheme://host[:port]
        String origin = originOf(uri);

        // 화이트리스트에 있는 origin만 허용
        return appProperties.getAllowedRedirectOrigins().stream()
                .anyMatch(allowed -> normalize(allowed).equals(normalize(origin)));
    }

    private String originOf(URI uri) {
        int port = uri.getPort();
        if (port == -1) return uri.getScheme() + "://" + uri.getHost();
        return uri.getScheme() + "://" + uri.getHost() + ":" + port;
    }

    private String normalize(String s) {
        if (s == null) return null;
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }
}
