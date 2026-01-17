package com.example.snowman.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.snowman.entity.Diary;

public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {
	List<Diary> findAllByUserId(Long userId);

	@Query("""
			SELECT DISTINCT extract(day from d.diaryDate) 
			FROM Diary d
			WHERE d.userId = :userId
			  AND d.diaryDate >= :start
			  AND d.diaryDate < :end
		""")
	List<Integer> findWrittenDays(
			@Param("userId") Long userId,
			@Param("start") LocalDate start,
			@Param("end") LocalDate end
			);

	@Query("""
			SELECT d
			FROM Diary d
			WHERE d.userId = :userId
			  AND d.diaryDate = :date
		""")
	List<Diary> findDiaryByDate(
		@Param("userId") Long userId,
		@Param("date") LocalDate date
	);

	Diary findByDiaryId(Long diaryId);
}
