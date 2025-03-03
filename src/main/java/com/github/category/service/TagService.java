package com.github.category.service;


import com.github.category.repository.QuestionRepository;
import com.github.category.repository.TagRepository;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.repository.entity.TagEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final QuestionRepository questionRepository;
    private static final Logger logger = LoggerFactory.getLogger(TagService.class);

    public String getTagsByQuestionId(int questionId) {
        Optional<QuestionEntity> question = questionRepository.findById(questionId);

        if (question.isPresent()) {
            Set<TagEntity> tags = question.get().getTags();
            // 태그 목록을 쉼표로 구분하여 반환
            return "Tags: " + tags.stream()
                    .map(TagEntity::getTag)
                    .collect(Collectors.joining(", "));
        } else {
            // 질문이 없으면 적절한 메시지 반환
            return "Question not found for ID: " + questionId;
        }
    }
}
