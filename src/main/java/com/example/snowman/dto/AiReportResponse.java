package com.example.snowman.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.snowman.entity.AiReport;
import com.example.snowman.entity.AiReport.PeriodType;
import com.example.snowman.entity.EmotionScores;

public record AiReportResponse(
	Long reportId,
	Long userId,
	PeriodType periodType,
	LocalDate periodStart,
	LocalDate periodEnd,
	EmotionScoresResponse scores,
	String anxietySummary,
	String calmSummary,
	String joySummary,
	String sadnessSummary,
	String angerSummary,
	String reportContent,
	LocalDateTime createdAt
) {
	public static AiReportResponse of(AiReport report) {
		return new AiReportResponse(
			report.getReportId(),
			report.getUserId(),
			report.getPeriodType(),
			report.getPeriodStart(),
			report.getPeriodEnd(),
			EmotionScoresResponse.of(report.getScores()),
			report.getAnxietySummary(),
			report.getCalmSummary(),
			report.getJoySummary(),
			report.getSadnessSummary(),
			report.getAngerSummary(),
			report.getReportContent(),
			report.getCreatedAt()
		);
	}

	public static List<AiReportResponse> of(List<AiReport> reports) {
		return reports.stream().map(AiReportResponse::of).toList();
	}

	public record EmotionScoresResponse(
		int anxiety,
		int calm,
		int joy,
		int sadness,
		int anger
	) {
		public static EmotionScoresResponse of(EmotionScores scores) {
			if (scores == null) {
				return null;
			}
			return new EmotionScoresResponse(
				scores.getAnxiety(),
				scores.getCalm(),
				scores.getJoy(),
				scores.getSadness(),
				scores.getAnger()
			);
		}
	}
}
