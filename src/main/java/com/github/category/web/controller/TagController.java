package com.github.category.web.controller;

import com.github.category.repository.QuestionRepository;
import com.github.category.repository.TagRepository;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.repository.entity.TagEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final QuestionRepository questionRepository;


    @Operation(summary = "Create a tag")
    @PostMapping
    public TagEntity createTag(@RequestBody TagEntity tagEntity) {
        TagEntity savedTag = tagRepository.save(tagEntity);
        return savedTag;
    }


    // 태그 조회 (질문에 해당하는 태그들 조회)
    @Operation(summary = "Get Tags")
    @GetMapping("/questions/{questionId}")
    public String getTagsByQuestionId(@PathVariable int questionId) {
        Optional<QuestionEntity> question = questionRepository.findById(questionId);

        return "Tag"+question;
    }

    // 질문에 태그 추가
//    @PostMapping("/assign")
//    public ResponseEntity<Void> assignTagToQuestion(@RequestParam int questionId, @RequestParam int tagId) {
//        Optional<QuestionEntity> question = questionRepository.findById(questionId);
//        Optional<TagEntity> tag = tagRepository.findById(tagId);
//
//        if (question.isPresent() && tag.isPresent()) {
//            question.get().getTags().add(tag.get());
//            tag.get().getQuestions().add(question.get());
//            questionRepository.save(question.get());
//            tagRepository.save(tag.get());
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}
