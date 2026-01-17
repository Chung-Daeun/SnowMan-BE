package com.example.snowman.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://172.30.1.39:3000",
                "https://snow-man-psi.vercel.app",
                "https://app.snowman.today"
        ));

        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));

        // 세션 쿠키(JSESSIONID) 기반이면 반드시 true
        config.setAllowCredentials(true);

        // (선택) 프론트에서 Set-Cookie 확인/디버깅 필요할 때 도움
        config.setExposedHeaders(List.of("Set-Cookie"));

        // preflight 캐시 (선택) - OPTIONS 요청 줄여줌
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
