package com.example.snowman.dto;

import com.example.snowman.entity.AiAnalysis;
import com.example.snowman.entity.AiReply;
import com.example.snowman.entity.Diary;
import com.example.snowman.entity.EmotionScores;

public record AiReplyResult(
	String replyContent,
	String summary,
	EmotionScoresResult scores
) {
	public AiReply toAiReply(Diary diary) {
		return AiReply.create(diary, replyContent);
	}

	public AiAnalysis toAiAnalysis(Diary diary) {
		EmotionScores emotionScores = scores != null ? scores.toEntity() : null;
		return AiAnalysis.create(diary, emotionScores, summary);
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
