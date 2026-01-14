package com.example.snowman.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.snowman.dto.DiaryResponse;
import com.example.snowman.dto.DiarySaveRequest;
import com.example.snowman.entity.Diary;
import com.example.snowman.entity.User;
import com.example.snowman.repository.DiaryJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final DiaryJpaRepository diaryJpaRepository;

	public List<DiaryResponse> getDiary(Long userId) {
		List<Diary> diaryList = diaryJpaRepository.findAllByUserId(userId);
		return DiaryResponse.of(diaryList);
	}

	public List<DiaryResponse> getDiaryByDate(User user, LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		List<Diary> diaryList =
			diaryJpaRepository.findDiaryByDateRange(
				user.getUserId(), start, end
			);
		return DiaryResponse.of(diaryList);
	}

	public String addDiary(User user, DiarySaveRequest diary) {
		diaryJpaRepository.save(Diary.create(user.getUserId(), diary.content()));
		return "작성이 성공적으로 완료되었습니다.";
	}
}
