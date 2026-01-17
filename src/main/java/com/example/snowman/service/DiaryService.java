package com.example.snowman.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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

	public DiaryResponse getDiaryById(Long diaryId) {
		Diary diaryList = diaryJpaRepository.findByDiaryId(diaryId);
		return DiaryResponse.of(diaryList);
	}

	public List<DiaryResponse> getDiaryByDate(User user, LocalDate date) {
//		LocalDate day = LocalDate.parse(date);

		return DiaryResponse.of(diaryJpaRepository.findDiaryByDate(user.getUserId(), date));
	}

	public List<Integer> getDiaryByMonth(User user, String date) {
		YearMonth yearMonth = YearMonth.parse(date);

		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.plusMonths(1).atDay(1);

		return diaryJpaRepository.findWrittenDays(user.getUserId(), start, end);
	}

	public Long addDiary(User user, DiarySaveRequest diary) {
		Diary save = diaryJpaRepository.save(Diary.create(user.getUserId(), diary.content()));
		return save.getDiaryId();
	}
}
