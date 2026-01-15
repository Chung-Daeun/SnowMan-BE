package com.example.snowman.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.snowman.common.ApiResponse;
import com.example.snowman.common.resolver.CurrentUser;
import com.example.snowman.dto.AiReplyCreateResponse;
import com.example.snowman.entity.User;
import com.example.snowman.service.AiReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiReplyController {

	private final AiReplyService aiReplyService;

	@PostMapping("/reply")
	public ApiResponse<AiReplyCreateResponse> generateReply(
		@CurrentUser User user,
		@RequestParam Long diaryId
	) {
		return ApiResponse.of(aiReplyService.createReply(user, diaryId));
	}
}
