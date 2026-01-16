package com.example.snowman.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.snowman.service.AiReportService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AiReportScheduler {

	private final AiReportService aiReportService;

	// 매주 월요일 00:10 (기본값) 주간 리포트 생성
	@Scheduled(
		cron = "${app.scheduler.weekly-cron:0 10 0 * * MON}",
		zone = "${app.scheduler.timezone:Asia/Seoul}"
	)
	public void runWeeklyReport() {
		aiReportService.createWeeklyReports();
	}

	// 매월 1일 00:20 (기본값) 월간 리포트 생성
	@Scheduled(
		cron = "${app.scheduler.monthly-cron:0 20 0 1 * *}",
		zone = "${app.scheduler.timezone:Asia/Seoul}"
	)
	public void runMonthlyReport() {
		aiReportService.createMonthlyReports();
	}
}
