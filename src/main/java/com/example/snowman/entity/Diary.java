package com.example.snowman.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "diary_id")
	private Long diaryId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "content")
	private String content;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "diary_date", nullable = false)
	private LocalDate diaryDate;

	@PrePersist
	public void prePersist(){
		if (createdAt == null) createdAt = LocalDateTime.now();
		if (diaryDate == null) diaryDate = createdAt.toLocalDate();
	}

	@OneToOne(mappedBy = "diary")
	private AiReply aiReply;

	private Diary(Long userId, String content){
		this.userId = userId;
		this.content = content;
		this.createdAt = LocalDateTime.now();
	}

	public static Diary create(Long userId, String content){
		return new Diary(userId, content);
	}
}
