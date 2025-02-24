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
public class LogDTO {
    private int logId;
    private int questionId;
    private String questionText;
    private int categoryId;
    private Boolean matchedKeywords;
    private ZonedDateTime createdAt;
}