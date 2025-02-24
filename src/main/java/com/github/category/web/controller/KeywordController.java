package com.github.category.web.controller;

import com.github.category.service.KeywordService;
import com.github.category.web.dto.KeywordBody;
import com.github.category.web.dto.KeywordDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/keywords")
@RequiredArgsConstructor
@RestController
public class KeywordController {

    private final KeywordService keywordService;
    // 키워드 전체 조회
    @GetMapping
    public List<KeywordDTO> getAllKeywords() {
        return keywordService.getAllKeywords();
    }


    @GetMapping({"/{categoryId}")
    public List<KeywordDTO> getKeywordsByCategory(int categoryId) {
        return  keywordService.getKeywordsByCategory(categoryId);
    }

    @Operation(summary="Add a keyword")
    @PostMapping("/{categoryId}")
    public KeywordDTO createKeyword(@PathVariable int categoryId, @RequestBody KeywordBody keywordBody) {
        return keywordService.createKeyword(categoryId, keywordBody);
    }

    // 키워드 삭제
    @DeleteMapping("/{categoryId}/{keyword}")
    public void deleteKeyword(@PathVariable Long categoryId, @PathVariable String keyword) {
        boolean deleted = keywordService.deleteKeyword(categoryId, keyword);
        if (!deleted) {
            throw new NotFoundException("Keyword not found");
        }
    }
}
