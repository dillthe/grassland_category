package com.github.category.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KeywordDTO {
    private int keywordId;
    private int categoryId;
    private String categoryName;
    private String keyword;
}