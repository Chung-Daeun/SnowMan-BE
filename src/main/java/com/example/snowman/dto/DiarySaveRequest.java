package com.example.snowman.dto;

public record DiarySaveRequest(
	Long userId,
	String content
) {
}
