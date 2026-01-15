package com.example.snowman.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.snowman.dto.AiReplyCreateResponse;
import com.example.snowman.entity.AiAnalysis;
import com.example.snowman.entity.AiReply;
import com.example.snowman.entity.Diary;
import com.example.snowman.entity.User;
import com.example.snowman.repository.AiAnalysisRepository;
import com.example.snowman.repository.AiReplyRepository;
import com.example.snowman.repository.DiaryJpaRepository;
import com.example.snowman.repository.UserRepository;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class AiReplyServiceTest {

	@Autowired
	private AiReplyService aiReplyService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DiaryJpaRepository diaryJpaRepository;

	@Autowired
	private AiReplyRepository aiReplyRepository;

	@Autowired
	private AiAnalysisRepository aiAnalysisRepository;

	@AfterEach
	void tearDown() {
		aiAnalysisRepository.deleteAll();
		aiReplyRepository.deleteAll();
		diaryJpaRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@Transactional
	@DisplayName("OpenAi로 부터 Diary content에 해당하는 내용을 받아옵니다.")
	void createReplyTest() {
		//given
		User user = userRepository.save(User.create("sub-integration-test"));
		Diary diary = diaryJpaRepository.save(Diary.create(user.getUserId(), "오늘 두쫀쿠를 먹었다 정말 맛있었."));

		//when
		AiReplyCreateResponse response = aiReplyService.createReply(user, diary.getDiaryId());

		//then
		assertThat(user.getUserId()).isEqualTo(1L);
		assertThat(response.aiReply()).isNotNull();
		assertThat(response.aiReply().replyContent()).isNotBlank();
		AiReply saved = aiReplyRepository.findByDiary(diary).orElseThrow();
		System.out.println(saved.getReplyId());
		System.out.println(saved.getReplyContent());
		assertThat(saved.getReplyContent()).isNotBlank();

		AiAnalysis savedAnalysis = aiAnalysisRepository.findAll().stream().findFirst().orElseThrow();
		assertThat(savedAnalysis.getSummary()).isNotBlank();
		assertThat(savedAnalysis.getScores()).isNotNull();
		System.out.println("anxiety=" + savedAnalysis.getScores().getAnxiety() + "%");
		System.out.println("calm=" + savedAnalysis.getScores().getCalm() + "%");
		System.out.println("joy=" + savedAnalysis.getScores().getJoy() + "%");
		System.out.println("sadness=" + savedAnalysis.getScores().getSadness() + "%");
		System.out.println("anger=" + savedAnalysis.getScores().getAnger() + "%");
	}
}
