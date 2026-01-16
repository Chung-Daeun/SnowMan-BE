package com.example.snowman.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.snowman.dto.AiReportResult;
import com.example.snowman.entity.AiAnalysis;
import com.example.snowman.entity.AiReport;
import com.example.snowman.entity.Diary;
import com.example.snowman.entity.EmotionScores;
import com.example.snowman.entity.User;
import com.example.snowman.repository.AiAnalysisRepository;
import com.example.snowman.repository.AiReportRepository;
import com.example.snowman.repository.DiaryJpaRepository;
import com.example.snowman.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(properties = {
	"app.scheduler.weekly-cron=*/1 * * * * *",
	"app.scheduler.monthly-cron=0 0 0 1 1 *",
	"app.scheduler.timezone=UTC"
})
class AiReportSchedulerIntegrationTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DiaryJpaRepository diaryJpaRepository;
	@Autowired
	private AiAnalysisRepository aiAnalysisRepository;
	@Autowired
	private AiReportRepository aiReportRepository;
	private User user;

	@BeforeEach
	void setUpWeeklyData() {
		user = userRepository.save(User.create("sub-scheduler-test"));
		for (int i = 0; i < 7; i++) {
			LocalDateTime createdAt = LocalDate.now().minusDays(i + 1).atTime(12, 0);
			Diary diary = diaryJpaRepository.save(Diary.create(user.getUserId(), "내용 " + i));
			ReflectionTestUtils.setField(diary, "createdAt", createdAt);
			diaryJpaRepository.save(diary);

			EmotionScores scores = new EmotionScores(20, 20, 20, 20, 20);
			AiAnalysis analysis = AiAnalysis.create(diary, scores, "요약 " + i);
			aiAnalysisRepository.save(analysis);
		}
	}

	@AfterEach
	void tearDown() {
		aiReportRepository.deleteAll();
		aiAnalysisRepository.deleteAll();
		diaryJpaRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
	void schedulerCreatesWeeklyReportFromAnalyses() throws Exception {
		List<AiAnalysis> weeklyAnalyses = loadWeeklyAnalyses();
		assertThat(weeklyAnalyses).hasSize(7);
		weeklyAnalyses.forEach(analysis -> {
			System.out.println("[weekly-analysis] diaryCreatedAt="
				+ analysis.getDiary().getCreatedAt()
				+ ", scores=" + analysis.getScores()
				+ ", summary=" + analysis.getSummary());
		});
		AiReport report = waitForReport();
		System.out.println("[ai-report] userId=" + report.getUserId()
			+ ", periodType=" + report.getPeriodType()
			+ ", period=" + report.getPeriodStart() + "~" + report.getPeriodEnd()
			+ ", scores=" + report.getScores()
			+ ", anxietySummary=" + report.getAnxietySummary()
			+ ", calmSummary=" + report.getCalmSummary()
			+ ", joySummary=" + report.getJoySummary()
			+ ", sadnessSummary=" + report.getSadnessSummary()
			+ ", angerSummary=" + report.getAngerSummary()
			+ ", reportContent=" + report.getReportContent());
		assertThat(report.getUserId()).isEqualTo(user.getUserId());
		assertThat(report.getScores()).isNotNull();
		assertThat(report.getAnxietySummary()).isNotBlank();
		assertThat(report.getCalmSummary()).isNotBlank();
		assertThat(report.getJoySummary()).isNotBlank();
		assertThat(report.getSadnessSummary()).isNotBlank();
		assertThat(report.getAngerSummary()).isNotBlank();
		assertThat(report.getReportContent()).isNotBlank();
	}

	private List<AiAnalysis> loadWeeklyAnalyses() {
		LocalDate endDate = LocalDate.now().minusDays(1);
		LocalDate startDate = endDate.minusDays(6);
		LocalDateTime start = startDate.atStartOfDay();
		LocalDateTime end = endDate.plusDays(1).atStartOfDay();
		return aiAnalysisRepository.findByUserIdAndDiaryCreatedAtBetween(
			user.getUserId(),
			start,
			end
		);
	}

	private AiReport waitForReport() throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			List<AiReport> reports = aiReportRepository.findAll();
			if (!reports.isEmpty()) {
				return reports.get(0);
			}
			Thread.sleep(500);
		}
		throw new AssertionError("AiReport가 생성되지 않았습니다.");
	}
}
