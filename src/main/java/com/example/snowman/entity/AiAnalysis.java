package com.example.snowman.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_analysis")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AiAnalysis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "analysis_id")
	private Long analysisId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary_id", nullable = false)
	private Diary diary;

	@Embedded
	private EmotionScores scores;

	@Column(name = "summary")
	private String summary;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	private AiAnalysis(Diary diary, EmotionScores scores, String summary) {
		this.diary = diary;
		this.scores = scores;
		this.summary = summary;
		this.createdAt = LocalDateTime.now();
		if (scores != null) {
			scores.validateTotal();
		}
	}

	public static AiAnalysis create(Diary diary, EmotionScores scores, String summary) {
		return new AiAnalysis(diary, scores, summary);
	}

	public void update(EmotionScores scores, String summary) {
		this.scores = scores;
		this.summary = summary;
		if (scores != null) {
			scores.validateTotal();
		}
	}
}
