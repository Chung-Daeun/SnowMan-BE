package com.example.snowman.service.generator;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.example.snowman.dto.AiReportResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AiReportGenerator {

	private final ChatClient chatClient;

	public AiReportResult generate(String prompt) {
		return chatClient.prompt()
			.user(prompt)
			.call()
			.entity(AiReportResult.class);
	}
}
