package com.example.snowman.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.snowman.common.ApiResponse;
import com.example.snowman.common.resolver.CurrentUser;
import com.example.snowman.dto.AiReportResponse;
import com.example.snowman.entity.User;
import com.example.snowman.service.AiReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai/report")
@RequiredArgsConstructor
public class AiReportController {

	private final AiReportService aiReportService;

	@GetMapping("/weekly")
	public ApiResponse<List<AiReportResponse>> getWeeklyReports(@CurrentUser User user) {
		return ApiResponse.of(aiReportService.getWeeklyReports(user));
	}

	@GetMapping("/monthly")
	public ApiResponse<List<AiReportResponse>> getMonthlyReports(@CurrentUser User user) {
		return ApiResponse.of(aiReportService.getMonthlyReports(user));
	}

	@GetMapping("/weekly/run")
	public ApiResponse<String> runWeeklyReport() {
		aiReportService.createWeeklyReports();
		return ApiResponse.of("weekly report triggered");
	}

	@GetMapping("/monthly/run")
	public ApiResponse<String> runMonthlyReport() {
		aiReportService.createMonthlyReports();
		return ApiResponse.of("monthly report triggered");
	}
}
