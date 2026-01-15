package com.example.snowman.service.generator;

import com.example.snowman.dto.AiReplyResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AiReplyGenerator {

	private final ChatClient chatClient;

	public AiReplyResult generate(String prompt) {
		return chatClient.prompt()
			.user(prompt)
			.call()
			.entity(AiReplyResult.class);
	}
}
