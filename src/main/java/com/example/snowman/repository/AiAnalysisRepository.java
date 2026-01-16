package com.example.snowman.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.snowman.entity.AiAnalysis;

@Repository
public interface AiAnalysisRepository extends JpaRepository<AiAnalysis, Long> {
	@Query("""
		SELECT a
		FROM AiAnalysis a
		JOIN FETCH a.diary d
		WHERE d.userId = :userId
		  AND d.createdAt >= :start
		  AND d.createdAt < :end
	""")
	List<AiAnalysis> findByUserIdAndDiaryCreatedAtBetween(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);
}
