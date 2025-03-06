package com.github.category.web.dto;

import com.github.category.repository.entity.TagEntity;
import lombok.*;

import java.util.List;
import java.util.Set;

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
    private Set<String> tags;
    private String createdAt;
    //formatted Time, 타임존에 맞게 시간 출력되도록 DTO만 String으로 변환
}