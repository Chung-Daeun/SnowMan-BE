package com.example.snowman.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.snowman.entity.AiReport;
import com.example.snowman.entity.AiReport.PeriodType;
import com.example.snowman.entity.User;
import com.example.snowman.entity.AiAnalysis;
import com.example.snowman.repository.AiReportRepository;
import com.example.snowman.repository.AiAnalysisRepository;
import com.example.snowman.repository.UserRepository;
import com.example.snowman.dto.AiReportResult;
import com.example.snowman.dto.AiReportResponse;
import com.example.snowman.service.generator.AiReportGenerator;
import com.example.snowman.service.generator.AiReportPromptGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiReportService {

	private final UserRepository userRepository;
	private final AiAnalysisRepository aiAnalysisRepository;
	private final AiReportRepository aiReportRepository;
	private final AiReportGenerator aiReportGenerator;

	@Transactional
	public void createWeeklyReports() {
		LocalDate endDate = LocalDate.now().minusDays(1);
		LocalDate startDate = endDate.minusDays(6);
		createReports(PeriodType.WEEKLY, startDate, endDate);
	}

	@Transactional
	public void createMonthlyReports() {
		LocalDate endDate = LocalDate.now().minusDays(1);
		LocalDate startDate = endDate.with(TemporalAdjusters.firstDayOfMonth());
		createReports(PeriodType.MONTHLY, startDate, endDate);
	}

	@Transactional(readOnly = true)
	public List<AiReportResponse> getWeeklyReports(User user) {
		List<AiReport> reports = aiReportRepository.findByUserIdAndPeriodTypeOrderByPeriodStartDesc(
			user.getUserId(),
			PeriodType.WEEKLY
		);
		return AiReportResponse.of(reports);
	}

	@Transactional(readOnly = true)
	public List<AiReportResponse> getMonthlyReports(User user) {
		List<AiReport> reports = aiReportRepository.findByUserIdAndPeriodTypeOrderByPeriodStartDesc(
			user.getUserId(),
			PeriodType.MONTHLY
		);
		return AiReportResponse.of(reports);
	}

	private void createReports(PeriodType periodType, LocalDate startDate, LocalDate endDate) {
		List<User> users = userRepository.findAll();
		for (User user : users) {
			if (aiReportRepository.existsByUserIdAndPeriodTypeAndPeriodStartAndPeriodEnd(
				user.getUserId(),
				periodType,
				startDate,
				endDate
			)) {
				continue;
			}

			LocalDateTime start = startDate.atStartOfDay();
			LocalDateTime end = endDate.plusDays(1).atStartOfDay();
			List<AiAnalysis> analyses = aiAnalysisRepository.findByUserIdAndDiaryCreatedAtBetween(
				user.getUserId(),
				start,
				end
			);
			if (analyses.isEmpty()) {
				continue;
			}

			String prompt = AiReportPromptGenerator.buildPrompt(analyses, periodType, startDate, endDate);
			AiReportResult result = aiReportGenerator.generate(prompt);
			if (result == null) {
				continue;
			}

			AiReport report = new AiReport(
				null,
				user.getUserId(),
				periodType,
				startDate,
				endDate,
				result.toEmotionScores(),
				result.anxietySummary(),
				result.calmSummary(),
				result.joySummary(),
				result.sadnessSummary(),
				result.angerSummary(),
				result.reportContent(),
				LocalDateTime.now()
			);
			aiReportRepository.save(report);
		}
	}
}
