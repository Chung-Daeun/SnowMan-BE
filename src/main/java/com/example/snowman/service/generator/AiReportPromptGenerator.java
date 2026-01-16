package com.example.snowman.service.generator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.example.snowman.entity.AiAnalysis;
import com.example.snowman.entity.AiReport.PeriodType;

public class AiReportPromptGenerator {

	public static String buildPrompt(List<AiAnalysis> analyses, PeriodType periodType, LocalDate start, LocalDate end) {
		String periodLabel = periodType == PeriodType.WEEKLY ? "주간" : "월간";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String analysisItems = analyses.stream()
			.map(analysis -> {
				String date = analysis.getDiary().getCreatedAt().toLocalDate().format(formatter);
				String scores = analysis.getScores() != null
					? "불안 " + analysis.getScores().getAnxiety() + "%, "
					+ "평온 " + analysis.getScores().getCalm() + "%, "
					+ "기쁨 " + analysis.getScores().getJoy() + "%, "
					+ "슬픔 " + analysis.getScores().getSadness() + "%, "
					+ "분노 " + analysis.getScores().getAnger() + "%"
					: "점수 없음";
				String summary = analysis.getSummary() != null ? analysis.getSummary() : "요약 없음";
				return "[" + date + "] " + scores + " / 요약: " + summary;
			})
			.collect(Collectors.joining("\n- "));

		return "다음은 사용자의 " + periodLabel + " 감정 분석 결과입니다.\n"
			+ "기간: " + start + " ~ " + end + "\n\n"
			+ "요청:\n"
			+ "1) 기간 전체의 감정 점수 평균을 계산하고 EmotionScores JSON으로 반환하세요.\n"
			+ "2) 불안/평온/기쁨/슬픔/분노 각각에 대해 간단한 원인 요약을 1~2문장으로 작성하세요.\n"
			+ "3) 기간 전체의 요약 리포트를 3~5문장으로 작성하세요.\n"
			+ "4) 결과는 JSON 형식으로만 반환하세요.\n"
			+ "필드: scores{anxiety,calm,joy,sadness,anger}, "
			+ "anxietySummary, calmSummary, joySummary, sadnessSummary, angerSummary, reportContent\n\n"
			+ "- " + analysisItems;
	}
}
