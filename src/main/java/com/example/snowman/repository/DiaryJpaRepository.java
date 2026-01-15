package com.example.snowman.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.snowman.entity.Diary;

public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {
	List<Diary> findAllByUserId(Long userId);

	@Query("""
			SELECT d
			FROM Diary d
			WHERE d.userId = :userId
			  AND d.createdAt >= :start
			  AND d.createdAt < :end
			  AND d.deletedAt IS NULL
		""")
	List<Diary> findDiaryByDateRange(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	@Query("""
			SELECT d
			FROM Diary d
			WHERE d.diaryId = :diaryId
			  AND d.deletedAt IS NULL
		""")
	Diary findByDiaryId(@Param("diaryId") Long diaryId);
}
