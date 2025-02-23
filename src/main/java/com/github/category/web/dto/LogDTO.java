package com.github.category.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.security.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
    private Long logId;
    private Long questionId;
    private String questionText;
    private Long categoryId;
    private Boolean matchedKeywords;
    private Timestamp createdAt;
}