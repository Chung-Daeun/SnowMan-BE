package com.example.snowman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiReplyController {

    private final ChatClient chatClient;

    @GetMapping("/ai/test")
    public String test(@RequestParam(defaultValue = "한국어로 1문장으로 인사해줘") String prompt) {

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}