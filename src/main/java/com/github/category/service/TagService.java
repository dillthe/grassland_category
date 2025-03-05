package com.github.category.service;


import com.github.category.repository.QuestionRepository;
import com.github.category.repository.TagRepository;
import com.github.category.repository.entity.KeywordEntity;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.repository.entity.TagEntity;
import com.github.category.service.mapper.KeywordMapper;
import com.github.category.service.mapper.TagMapper;
import com.github.category.web.dto.KeywordDTO;
import com.github.category.web.dto.TagDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final QuestionRepository questionRepository;
    private static final Logger logger = LoggerFactory.getLogger(TagService.class);


    //질문과 연관된 전체 태그 반환
    public String getTagsByQuestionId(int questionId) {
        Optional<QuestionEntity> question = questionRepository.findById(questionId);

        if (question.isPresent()) {
            Set<TagEntity> tags = question.get().getTags();

            return "Tags: " + tags.stream()
                    .map(TagEntity::getTag)
                    .collect(Collectors.joining(", "));
        } else {
            return "No question found for ID: " + questionId;
        }
    }

    //태그와 연관된 전체 질문 리스트 반환
    @Transactional
    public String getQuestionsByTagId(int tagId) {
        Optional<TagEntity> tag = tagRepository.findById(tagId);
        if (tag.isPresent()) {
            Set<QuestionEntity> questions = tag.get().getQuestions();
            // 질문 목록을 쉼표로 구분하여 반환
            return "Tag: " + tag.get().getTag() +"\n"+
                    "Questions related to this tag: \n" + questions
                    .stream()
                    .map(QuestionEntity::getQuestion)
                    .collect(Collectors.joining("\n"));
        } else {
            return "No tag found for ID: " + tagId;
        }
    }


    //전체 태그 반환 - 태그에 포함된 질문 수 내림차순 정렬
    //태그에 포함된 질문이 많아지면 나중에 해당 태그를 키워드로 넣을 수 있도록..!?
    public String getAllTags() {
        List<TagEntity> tagEntities = tagRepository.findAll();
        List<TagDTO> tagDTOs = TagMapper.INSTANCE.tagEntitiesToTagDTOs(tagEntities);
        tagDTOs.sort(Comparator.comparingInt(TagDTO::getQuestionCount).reversed());

        return "Tags: " + getTagNames(tagDTOs);
    }
    private String getTagNames(List<TagDTO> tagDTOList) {
        return tagDTOList.stream()
                .map(tagDTO -> tagDTO.getTag() + " (" + tagDTO.getQuestionCount() + " questions)")
                .collect(Collectors.joining(", "));
    }


    public String deleteAllTags() {
        tagRepository.deleteAll();
        return "All tags are deleted.";
    }
}
