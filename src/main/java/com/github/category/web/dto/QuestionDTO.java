package com.github.category.web.dto;

import lombok.*;

import java.security.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuestionDTO {
    private int questionId;
    private String question;
    private int categoryId;
    private String categoryName;
    private String createdAt; //formatted Time, 타임존에 맞게 시간 출력되도록 DTO만 String으로 변환
}