package com.github.category.web.dto;

import lombok.*;

import java.security.Timestamp;
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
    private ZonedDateTime createdAt;
}