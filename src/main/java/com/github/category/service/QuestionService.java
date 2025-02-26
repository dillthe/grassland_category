package com.github.category.service;


import com.github.category.repository.CategoryRepository;
import com.github.category.repository.QuestionRepository;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.service.exceptions.NotAcceptException;
import com.github.category.service.exceptions.NotFoundException;
import com.github.category.service.mapper.QuestionMapper;
import com.github.category.web.dto.QuestionBody;
import com.github.category.web.dto.QuestionDTO;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;


    public QuestionEntity createQuestion(String question, String userTimeZone) {
        // 사용자의 시간대에 맞는 ZonedDateTime 생성
        ZonedDateTime userZonedDateTime = ZonedDateTime.now(ZoneId.of(userTimeZone));

        // 새로운 질문 생성
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestion(question);
        questionEntity.setCreatedAt(userZonedDateTime); // 사용자의 시간대 반영

        // 질문 저장
        return questionRepository.save(questionEntity);
    }

    @Transactional
    public String createQuestion(QuestionBody questionBody) {
        QuestionEntity questionEntity = QuestionMapper.INSTANCE.idAndQuestionBodyToQuestionEntity(null,questionBody);
        //질문에서 키워드 뽑아 카테고리 분류하기
        String categoryName = categoryService.determineCategory(questionBody.getQuestion());
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryName)
                        .orElseThrow(()->new NotFoundException("Can't find the category" + categoryName));
        questionEntity.setCategoryEntity(categoryEntity);
        QuestionEntity questionCreated = questionRepository.save(questionEntity);
        QuestionDTO questionDTO = QuestionMapper.INSTANCE.questionEntityToQuestionDTO(questionCreated);
        return "Question is created: " + questionDTO.getQuestion() + ", Category Name: " + questionDTO.getCategoryName();
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
    public QuestionDTO getQuestionById(int questionId) {
        QuestionEntity existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(()->new NotAcceptException("Question Id doesn't exist"));
        QuestionDTO questionDTO = QuestionMapper.INSTANCE.questionEntityToQuestionDTO(existingQuestion);
        return questionDTO;
    }



}
