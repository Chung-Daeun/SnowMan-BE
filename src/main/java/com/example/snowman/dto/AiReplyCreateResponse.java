package com.example.snowman.dto;

import java.time.LocalDateTime;

import com.example.snowman.entity.AiAnalysis;
import com.example.snowman.entity.AiReply;
import com.example.snowman.entity.EmotionScores;

public record AiReplyCreateResponse(
	AiReplyResponse aiReply,
	AiAnalysisResponse aiAnalysis
) {
	public static AiReplyCreateResponse of(AiReply aiReply, AiAnalysis aiAnalysis) {
		return new AiReplyCreateResponse(
			AiReplyResponse.of(aiReply),
			AiAnalysisResponse.of(aiAnalysis)
		);
	}

	public record AiReplyResponse(
		Long replyId,
		String replyContent,
		LocalDateTime createdAt
	) {
		public static AiReplyResponse of(AiReply aiReply) {
			if (aiReply == null) {
				return null;
			}
			return new AiReplyResponse(
				aiReply.getReplyId(),
				aiReply.getReplyContent(),
				aiReply.getCreatedAt()
			);
		}
	}

	public record AiAnalysisResponse(
		Long analysisId,
		String summary,
		EmotionScoresResponse scores,
		LocalDateTime createdAt
	) {
		public static AiAnalysisResponse of(AiAnalysis aiAnalysis) {
			if (aiAnalysis == null) {
				return null;
			}
			return new AiAnalysisResponse(
				aiAnalysis.getAnalysisId(),
				aiAnalysis.getSummary(),
				EmotionScoresResponse.of(aiAnalysis.getScores()),
				aiAnalysis.getCreatedAt()
			);
		}
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
