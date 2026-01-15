package com.example.snowman.service;

import com.example.snowman.dto.AiReplyCreateResponse;
import com.example.snowman.dto.AiReplyResult;
import com.example.snowman.entity.AiAnalysis;
import com.example.snowman.repository.AiAnalysisRepository;
import com.example.snowman.service.generator.AiReplyGenerator;
import com.example.snowman.service.generator.PromptGenerator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.snowman.entity.AiReply;
import com.example.snowman.entity.Diary;
import com.example.snowman.entity.User;
import com.example.snowman.repository.AiReplyRepository;
import com.example.snowman.repository.DiaryJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiReplyService {

	private final AiReplyRepository aiReplyRepository;
	private final AiAnalysisRepository aiAnalysisRepository;
	private final DiaryJpaRepository diaryJpaRepository;
	private final AiReplyGenerator aiReplyGenerator;

	//외부 API 통신하는 부분이므로 Async 어노테이션를 통해 비동기 적용
	@Async
	@Transactional
	public AiReplyCreateResponse createReply(User user, Long diaryId) {
		Diary diary = getDiaryForUser(user, diaryId);

		if (aiReplyRepository.existsByDiary(diary)) {
			throw new IllegalStateException("이미 reply가 존재합니다.");
		}

		String prompt = PromptGenerator.buildPrompt(diary);
		AiReplyResult aiReplyResult = aiReplyGenerator.generate(prompt);

		AiReply aiReply = aiReplyResult.toAiReply(diary);
		AiAnalysis aiAnalysis = aiReplyResult.toAiAnalysis(diary);
		AiReply savedReply = aiReplyRepository.save(aiReply);
		AiAnalysis savedAnalysis = aiAnalysisRepository.save(aiAnalysis);

		return AiReplyCreateResponse.of(diaryId, savedReply, savedAnalysis);
	}


	private Diary getDiaryForUser(User user, Long diaryId) {
		Diary diary = diaryJpaRepository.findById(diaryId)
			.orElseThrow(() -> new IllegalArgumentException("Diary not found."));

		//user 검증
		if (!diary.getUserId().equals(user.getUserId())) {
			throw new IllegalArgumentException("Diary does not belong to user.");
		}

		return diary;
	}
}
