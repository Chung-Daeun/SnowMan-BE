package com.example.snowman.dto;

import com.example.snowman.entity.EmotionScores;

public record AiReportResult(
	EmotionScoresResult scores,
	String anxietySummary,
	String calmSummary,
	String joySummary,
	String sadnessSummary,
	String angerSummary,
	String reportContent
) {
	public EmotionScores toEmotionScores() {
		return scores != null ? scores.toEntity() : null;
	}

	public record EmotionScoresResult(
		int anxiety,
		int calm,
		int joy,
		int sadness,
		int anger
	) {
		public EmotionScores toEntity() {
			return new EmotionScores(anxiety, calm, joy, sadness, anger);
		}
	}
}
