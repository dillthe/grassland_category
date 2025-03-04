package com.github.category.web.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagDTO {
    private int tagId;
    private String tag;
    private int questionCount;
}
