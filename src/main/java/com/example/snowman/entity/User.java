package com.example.snowman.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    private String googleSub;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String nickname;

    @Column
    private LocalDate birthDate;

    private User(String googleSub) {
        this.googleSub = googleSub;
        this.createdAt = LocalDateTime.now();
    }

    public static User create(String googleSub) {
        return new User(googleSub);
    }

    public void updateProfile(String nickname, LocalDate birthDate) {
        this.nickname = nickname;
        this.birthDate = birthDate;
    }
}
