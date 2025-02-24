package com.github.category.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordBody {
    private int categoryId;
    private String keyword;
    private ZonedDateTime createdAt;
}