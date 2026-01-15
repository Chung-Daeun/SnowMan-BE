package com.example.snowman.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionScores {
    private int anxiety;
    private int calm;
    private int joy;
    private int sadness;
    private int anger;

    // 객체 내부에서 검증 로직을 가질 수 있음
    public void validateTotal() {
        if (anxiety + calm + joy + sadness + anger != 100) {
            throw new IllegalArgumentException("감정의 총합은 100이어야 합니다.");
        }
    }
}
