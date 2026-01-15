package com.example.snowman.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

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

	public DiaryResponse getDiaryById(Long diaryId) {
		Diary diary = diaryJpaRepository.findByDiaryId(diaryId);
		if (diary == null) {
			// deleted_at이 있는지 확인하기 위해 전체 조회
			Optional<Diary> deletedDiaryOpt = diaryJpaRepository.findById(diaryId);
			if (deletedDiaryOpt.isPresent()) {
				Diary deletedDiary = deletedDiaryOpt.get();
				if (deletedDiary.getDeletedAt() != null) {
					throw new IllegalStateException("삭제된 일기입니다.");
				}
			}
			throw new IllegalArgumentException("일기를 찾을 수 없습니다.");
		}
		return DiaryResponse.of(diary);
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

	public List<DiaryResponse> getDiaryByMonth(User user, YearMonth date) {
		LocalDateTime start = date.atDay(1).atStartOfDay();
		LocalDateTime end = date.plusMonths(1).atDay(1).atStartOfDay();
		List<Diary> diaryList =
			diaryJpaRepository.findDiaryByDateRange(
				user.getUserId(), start, end
			);
		return DiaryResponse.of(diaryList);
	}

	public Long addDiary(User user, DiarySaveRequest diary) {
		Diary save = diaryJpaRepository.save(Diary.create(user.getUserId(), diary.content()));
		return save.getDiaryId();
	}
}
