package com.github.category.web.controller;

import com.github.category.repository.entity.TagEntity;
import com.github.category.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;


    // 태그 조회 (질문에 해당하는 태그들 조회)
    @Operation(summary = "Get Tags by Question ID")
    @GetMapping("/question/{questionId}")
    public String getTagsByQuestionId(@PathVariable int questionId) {
        return tagService.getTagsByQuestionId(questionId);
    }

    @Operation(summary = "Get Questions by Tag Id")
    @GetMapping("/{tagId}")
    public String getQuestionsByTagId(@PathVariable int tagId) {
        return tagService.getQuestionByTagId(tagId);
    }
}