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
    //키워드 전체 조회 시 카테고리 명도 불러오고 싶은 경우 사용 - 키워드 중복 있는지 확인할 때 사용하기 좋음(findKeywords 메소드)
    private String categoryName;
    private String keyword;
}