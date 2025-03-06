package com.github.category.service;


import com.github.category.repository.CategoryRepository;
import com.github.category.repository.QuestionRepository;
import com.github.category.repository.TagRepository;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.repository.entity.TagEntity;
import com.github.category.service.exceptions.NotAcceptException;
import com.github.category.service.exceptions.NotFoundException;
import com.github.category.service.mapper.QuestionMapper;
import com.github.category.web.dto.QuestionBody;
import com.github.category.web.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CategoryService categoryService;
    private final KeywordService keywordService;
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);


    @Transactional
    public String createQuestion(QuestionBody questionBody) {
        QuestionEntity questionEntity = QuestionMapper.INSTANCE.idAndQuestionBodyToQuestionEntity(null, questionBody);

        // 질문에서 키워드를 뽑아 카테고리 결정
        String category = categoryService.determineCategory(questionBody.getQuestion());
        logger.info("Determined category: {}", category);
        //연관된 카테고리 이름 여러개일 경우 split
        String[] categoryNames = category.split(",");
        Set<TagEntity> tagEntities = new HashSet<>();
        // 매칭된 키워드 목록 가져오기
        List<String> matchedKeywords = categoryService.getMatchedKeywords(questionBody.getQuestion());


        for (String categoryName : categoryNames) {
            String trimmedCategoryName = (categoryName == null || categoryName.trim().isEmpty()) ? "기타" : categoryName.trim();

            // 카테고리 조회
            CategoryEntity categoryEntity = categoryRepository.findByName(trimmedCategoryName).orElse(null);

            if (categoryEntity != null) {
                questionEntity.setCategoryEntity(categoryEntity);
                logger.info("Category assigned. Category: {}, Keywords:{}", categoryEntity.getName(), matchedKeywords);
            } else {
                logger.warn("CategoryEntity is null. Saving as tag: {}", trimmedCategoryName);
            }

            // 태그 조회 및 생성 (람다식 내에서 사용될 변수는 effectively final 이어야 함)
            TagEntity tagEntity = tagRepository.findByTag(trimmedCategoryName)
                    .orElseGet(() -> {
                        TagEntity newTag = new TagEntity();
                        newTag.setTag(trimmedCategoryName.replaceAll(" ",""));
                        return tagRepository.save(newTag);
                    });

            tagEntities.add(tagEntity);
            tagEntity.getQuestions().add(questionEntity);
        }

        //매칭된 키워드가 있다면 이것도 태그로 저장
        tagEntities.addAll(keywordService.createTagsFromMatchedKeywords(matchedKeywords, questionEntity));

        questionEntity.setTags(tagEntities);
        QuestionEntity savedQuestion = questionRepository.save(questionEntity);

        QuestionDTO questionDTO = QuestionMapper.INSTANCE.questionEntityToQuestionDTO(savedQuestion);


        return String.format("Question is created: %s, Category: %s, Keyword : %s, Tags: %s",
                questionDTO.getQuestion(),
                questionDTO.getCategoryName(),
                categoryService.getMatchedKeywords(questionDTO.getQuestion()),
                String.join(", ", categoryNames));
    }


    //전체 질문 조회
    public List<QuestionDTO> getAllQuestions() {
        List<QuestionEntity> questionEntities = questionRepository.findAll();
        List<QuestionDTO> questionDTOs = QuestionMapper.INSTANCE.questionEntitiesToQuestionDTOs(questionEntities);
        return questionDTOs;
    }

    //카테고리별 전체 질문 조회
    public List<QuestionDTO> getAllQuestionsByCategory(int categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + categoryId + " not found"));

        List<QuestionEntity> questionEntities = questionRepository.findByCategoryEntity(categoryEntity);
        List<QuestionDTO> questionDTOs = QuestionMapper.INSTANCE.questionEntitiesToQuestionDTOs(questionEntities);
        return questionDTOs;
    }

    //질문 단건 조회
//    public QuestionDTO getQuestionById(int questionId) {
//        QuestionEntity existingQuestion = questionRepository.findById(questionId)
//                .orElseThrow(() -> new NotAcceptException("Question Id doesn't exist"));
//        QuestionDTO questionDTO = QuestionMapper.INSTANCE.questionEntityToQuestionDTO(existingQuestion);
//        return questionDTO;
//    }

    public QuestionDTO getQuestionById(int questionId, String userTimeZone) {
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotAcceptException("Question Id doesn't exist"));
        QuestionDTO questionDTO = QuestionMapper.INSTANCE.questionEntityToQuestionDTO(existingQuestion);
        //질문을 저장할 때는 UTC타임으로 저장이 되지만, 질문을 조회할 때는 사용자 위치에 맞게 시간대가 변환되어 조회됨.
        String formattedTime = TimeZoneConverter.convertToUserTimeZone(existingQuestion.getCreatedAt(), userTimeZone);
        questionDTO.setCreatedAt(formattedTime);
        return questionDTO;
    }


    public String deleteQuestion(int questionId) {
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("No Question found for this Id" + questionId));
        questionRepository.deleteById(existingQuestion.getQuestionId());
        return String.format("Question Id: %d, Question: %s is deleted.", existingQuestion.getQuestionId(), existingQuestion.getQuestion());
    }

    public String deleteAllQuestion() {
        questionRepository.deleteAll();
        return "All questions are deleted.";
    }
}