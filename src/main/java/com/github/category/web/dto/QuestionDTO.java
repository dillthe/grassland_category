package com.github.category.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Timestamp;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private int questionId;
    private String question;
    private int categoryId;
    private ZonedDateTime createdAt;
}