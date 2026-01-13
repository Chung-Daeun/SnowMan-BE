package com.example.snowman.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.snowman.dto.DiarySaveRequest;
import com.example.snowman.entity.DiaryEntity;
import com.example.snowman.repository.DiaryJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final DiaryJpaRepository diaryJpaRepository;

	public List<DiaryEntity> getDiary(Long userId) {
		return diaryJpaRepository.findAllByUserId(userId);
	}

	public DiaryEntity addDiary(DiarySaveRequest diary) {
		return diaryJpaRepository.save(new DiaryEntity(null, diary.userId(), LocalDateTime.now(), diary.content(), null));
	}
}
