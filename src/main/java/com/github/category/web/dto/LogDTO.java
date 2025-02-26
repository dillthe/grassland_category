package com.github.category.web.dto;

import lombok.*;

import java.security.Timestamp;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogDTO {
    private int logId;
    private int questionId;
    private String questionText;
    private int categoryId;
    private String categoryName;
    private Boolean matchedKeywords;
//    private String logLevel;
//    private String message;
    private ZonedDateTime createdAt;
}