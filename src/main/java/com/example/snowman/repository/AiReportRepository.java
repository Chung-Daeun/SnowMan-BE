package com.example.snowman.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.snowman.entity.AiReport;
import com.example.snowman.entity.AiReport.PeriodType;

@Repository
public interface AiReportRepository extends JpaRepository<AiReport, Long> {
	boolean existsByUserIdAndPeriodTypeAndPeriodStartAndPeriodEnd(
		Long userId,
		PeriodType periodType,
		LocalDate periodStart,
		LocalDate periodEnd
	);

	List<AiReport> findByUserIdAndPeriodTypeOrderByPeriodStartDesc(
		Long userId,
		PeriodType periodType
	);
}
