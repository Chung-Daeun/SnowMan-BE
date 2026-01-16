package com.example.snowman.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AiReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;
    public enum PeriodType {
        MONTHLY,
        WEEKLY
    }

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

	@Column(name = "period_end", nullable = false)
	private LocalDate periodEnd;

	@Embedded
	private EmotionScores scores;

	@Column(name = "anxiety_summary")
	private String anxietySummary;

	@Column(name = "calm_summary")
	private String calmSummary;

	@Column(name = "joy_summary")
	private String joySummary;

	@Column(name = "sadness_summary")
	private String sadnessSummary;

	@Column(name = "anger_summary")
	private String angerSummary;

	@Column(name = "report_content")
	private String reportContent;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

}
