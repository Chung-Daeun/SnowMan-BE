package com.example.snowman.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    /**
     * 로그인 성공 후 기본 리다이렉트 URL (fallback)
     * 예: http://localhost:3000/dashboard
     * 예: https://snow-man-psi.vercel.app/dashboard
     */
    private String oauth2SuccessRedirectUrl;

    /**
     * 허용할 프론트 Origin 화이트리스트
     * 예: http://localhost:3000
     * 예: http://172.30.1.39:3000
     * 예: https://snow-man-psi.vercel.app
     */
    private List<String> allowedRedirectOrigins = new ArrayList<>();
}
