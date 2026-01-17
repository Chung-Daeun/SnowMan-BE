package com.example.snowman.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.example.snowman.entity.AiReply;
import com.example.snowman.entity.Diary;

public record DiaryResponse (
	Long diaryId,
	Long userId,
	String content,
	AiReplyResponse aiReply,
	String time
){
	public static DiaryResponse of(Diary diary) {
		return new DiaryResponse(
			diary.getDiaryId(),
			diary.getUserId(),
			diary.getContent(),
			diary.getAiReply() != null ? AiReplyResponse.of(diary.getAiReply()) : null,
			diary.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
		);
	}

	public static List<DiaryResponse> of(List<Diary> diaryList) {
		return diaryList.stream().map(DiaryResponse::of).toList();
	}

	public record AiReplyResponse (
		Long aiReplyId,
		String replyContent,
		LocalDateTime createdAt
	){
		public static AiReplyResponse of(AiReply aiReply){
			return new AiReplyResponse(
				aiReply.getReplyId(),
				aiReply.getReplyContent(),
				aiReply.getCreatedAt()
			);
		}
	}
}
