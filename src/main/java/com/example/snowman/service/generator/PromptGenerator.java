package com.example.snowman.service.generator;

import com.example.snowman.entity.Diary;

public class PromptGenerator {

    public static String buildPrompt(Diary diary) {
        return "프롬프트도 사용자의 일기를 분석하여 두 가지 작업을 수행하세요.\n\n"
                + "1. 감정 분석: '불안, 평온, 기쁨, 슬픔, 분노' 5가지 감정의 비중을 퍼센트(%)로 계산하세요.\n"
                + "(5개 수치의 합은 반드시 정확히 100이어야 합니다.)\n\n"
                + "2. 공감 답장: 일기 내용에 깊이 공감하고 따뜻한 위로를 전하는 답장을 한국어로 3~5문장 작성하세요.\n\n"
                + diary.getContent();
    }
}
