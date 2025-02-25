package com.github.category.web.dto;

import lombok.*;

import java.security.Timestamp;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KeywordDTO {
    private int keywordId;
    private int categoryId;
    private String keyword;
}