package com.example.snowman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.snowman.entity.DiaryEntity;

public interface DiaryJpaRepository extends JpaRepository<DiaryEntity, Long> {
	List<DiaryEntity> findAllByUserId(Long userId);
}
