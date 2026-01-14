package com.example.snowman.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
