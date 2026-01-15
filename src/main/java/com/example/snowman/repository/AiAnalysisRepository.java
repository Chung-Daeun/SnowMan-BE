package com.example.snowman.repository;

import com.example.snowman.entity.AiAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiAnalysisRepository extends JpaRepository<AiAnalysis, Long> {
}
