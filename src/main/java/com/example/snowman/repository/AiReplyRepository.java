package com.example.snowman.repository;

import com.example.snowman.entity.AiReply;
import com.example.snowman.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiReplyRepository extends JpaRepository<AiReply, Long> {

    Optional<AiReply> findByDiary(Diary diary);

    boolean existsByDiary(Diary diary);
}
