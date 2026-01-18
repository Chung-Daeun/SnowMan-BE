package com.example.snowman.component;

import org.springframework.stereotype.Component;

@Component
public class TestLoginPrincipalFactory {
    public Object create() {
        return "test-user";
    }
}
