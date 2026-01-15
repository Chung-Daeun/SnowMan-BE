package com.example.snowman.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import com.example.snowman.dto.AiReplyCreateResponse;
import com.example.snowman.service.AiReplyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.snowman.common.ApiResponse;
import com.example.snowman.common.resolver.CurrentUser;
import com.example.snowman.dto.DiaryResponse;
import com.example.snowman.dto.DiarySaveRequest;
import com.example.snowman.entity.User;
import com.example.snowman.service.DiaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;
	private final AiReplyService aiReplyService;

	@GetMapping("/{diaryId}")
	public ApiResponse<DiaryResponse> getDiaryById(@CurrentUser User user, @PathVariable Long diaryId) {
		return ApiResponse.of(diaryService.getDiaryById(diaryId));
	}

	@GetMapping("/day")
	public ApiResponse<List<DiaryResponse>> getDiaryByDate(@CurrentUser User user, @RequestParam LocalDate date) {
		return ApiResponse.of(diaryService.getDiaryByDate(user, date));
	}

	@GetMapping("/month")
	public ApiResponse<List<DiaryResponse>> getDiaryByMonth(@CurrentUser User user, @RequestParam YearMonth date) {
		return ApiResponse.of(diaryService.getDiaryByMonth(user, date));
	}

	@PostMapping("/create")
	public ApiResponse<AiReplyCreateResponse> saveDiary(@CurrentUser User user, @RequestBody DiarySaveRequest diary) {
		Long diaryId = diaryService.addDiary(user, diary);
		// AI 답변은 비동기로 자동 생성
		aiReplyService.createReply(user, diaryId);
		// diaryId만 반환 (AI 답변은 비동기로 생성 중)
		return ApiResponse.of(new AiReplyCreateResponse(diaryId, null, null));
	}

	@PostMapping("/{diaryId}/ai-reply")
	public ApiResponse<Void> createAiReply(@CurrentUser User user, @PathVariable Long diaryId) {
		aiReplyService.createReply(user, diaryId);
		return ApiResponse.of(null);
	}
}
