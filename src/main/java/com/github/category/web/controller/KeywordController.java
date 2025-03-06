package com.github.category.web.controller;

import com.github.category.repository.entity.CategoryEntity;
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

    @Operation(summary="Get all keywords")
    @GetMapping
    public List<KeywordDTO> getAllKeywords() {
        return keywordService.getAllKeywords();
    }


    @Operation(summary="Get keywords by a category")
    @GetMapping("/{categoryId}")
    public List<KeywordDTO> getKeywordsByCategory(@PathVariable int categoryId) {
        return  keywordService.getKeywordsByCategory(categoryId);
    }

    @Operation(summary="Add a keyword")
    @PostMapping("/{categoryId}")
    public String createKeyword(@PathVariable int categoryId, @RequestBody KeywordBody keywordBody) {
        KeywordDTO keywordDTO = keywordService.createKeyword(categoryId, keywordBody);
        return keywordDTO + "is successfully added!";
    }

    @Operation(summary="Add many keywords")
    @PostMapping("/{categoryId}/batch")
    public String createKeywords(@PathVariable int categoryId, @RequestBody List<KeywordBody> keywordBodies) {
        List<KeywordDTO> keywordDTOs = keywordService.createKeywords(categoryId, keywordBodies);
        return "Keywords are added successfully!" + keywordDTOs.toString();
    }

    //키워드 이름으로 해당 카테고리 전부 조회
    // 동일 키워드가 여러 카테고리에 들어가있을 경우 조회하여 중복된 정보 삭제하기 위함
    //키워드명, 키워드가 포함된 카테고리 번호가 출력되면 여기서 필요없는 카테고리 번호로 아래 delete a keyword by keyword 메소드 사용하면 됨.
    @Operation(summary="Find keywords and their categories")
    @GetMapping("/find")
    public String findKeywords(@RequestBody KeywordBody keywordBody) {
        return keywordService.findKeyword(keywordBody);
    }

    // 키워드 명으로 삭제
    @Operation(summary="Delete a keyword by Keyword")
    @DeleteMapping("/{categoryId}")
    public String deleteKeyword(@PathVariable int categoryId, @RequestBody KeywordBody keywordBody) {
        return keywordService.deleteKeyword(categoryId, keywordBody);
    }

    //키워드 id로 삭제
    @Operation(summary="Delete a keyword by Id")
    @DeleteMapping("/{categoryId}/{keywordId}")
    public String deleteKeyword(@PathVariable int categoryId, @PathVariable int keywordId) {
        String deletion = keywordService.deleteKeywordById(categoryId, keywordId);
        return deletion;
    }

    //카테고리 내 전체 키워드 삭제
    @Operation(summary="Delete all keywords by Category")
    @DeleteMapping("/all/{categoryId}")
    public String deleteKeyword(@PathVariable int categoryId) {
        String deletion = keywordService.deleteAllKeywordsByCategory(categoryId);
        return deletion;
    }

}
