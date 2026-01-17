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
	public ApiResponse<List<DiaryResponse>> getDiaryByDate(@CurrentUser User user, @RequestParam String date) {
		return ApiResponse.of(diaryService.getDiaryByDate(user, date));
	}

	@GetMapping("/month")
	public ApiResponse<List<Integer>> getDiaryByMonth(@CurrentUser User user, @RequestParam String date) {
		return ApiResponse.of(diaryService.getDiaryByMonth(user, date));
	}

	@PostMapping("/create")
	public ApiResponse<Long> saveDiary(@CurrentUser User user, @RequestBody DiarySaveRequest diary) {
		Long diaryId = diaryService.addDiary(user, diary);
//		AiReplyCreateResponse response = aiReplyService.createReply(user, diaryId);
		aiReplyService.createReply(user, diaryId);

//		return ApiResponse.of(response);
		return ApiResponse.of(diaryId);
	}
}
