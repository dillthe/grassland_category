package com.github.category.web.controller;

import com.github.category.service.QuestionService;
import com.github.category.web.dto.QuestionBody;
import com.github.category.web.dto.QuestionDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/questions")
@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "Create a question")
    @PostMapping
    public String createQuestion(@RequestBody QuestionBody questionBody) {
        QuestionDTO createdQuestion = questionService.createQuestion(questionBody);
        return "Question created: " + createdQuestion;
    }
    @Operation(summary = "Get all questions")
    @GetMapping
    public List<QuestionDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @Operation(summary = "Get all questions by category")
    @GetMapping("/{categoryId}")
    public List<QuestionDTO> getAllQuestionsByCategory(@PathVariable int categoryId) {
        return questionService.getAllQuestionsByCategory(categoryId);
    }

    @Operation(summary = "Get one question by Id")
    @GetMapping("/{questionId}")
    public QuestionDTO getQuestion(@PathVariable int questionId) {
        QuestionDTO questionDTO = questionService.getQuestionById(questionId);
        return questionDTO;
    }

//    @Operation(summary = "Delete a question")
//    @DeleteMapping("/{questionId}")
//    public String deleteQuestion(@PathVariable int questionId) {
//        String deletion = questionService.deleteQuestion(questionId);
//        return deletion;
//    }
}