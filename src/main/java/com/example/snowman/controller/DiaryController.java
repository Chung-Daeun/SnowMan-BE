package com.example.snowman.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.snowman.common.ApiResponse;
import com.example.snowman.dto.DiarySaveRequest;
import com.example.snowman.entity.DiaryEntity;
import com.example.snowman.service.DiaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	@GetMapping("/{userId}")
	public ApiResponse<List<DiaryEntity>> getDiary(@PathVariable Long userId) {
		return ApiResponse.of(diaryService.getDiary(userId));
	}

	@PostMapping("/")
	public ApiResponse<DiaryEntity> saveDiary(@RequestBody DiarySaveRequest diary) {
		return ApiResponse.of(diaryService.addDiary(diary));
	}
}
