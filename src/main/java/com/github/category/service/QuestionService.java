package com.github.category.service;


import com.github.category.repository.QuestionRepository;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.service.exceptions.NotAcceptException;
import com.github.category.service.mapper.QuestionMapper;
import com.github.category.web.dto.QuestionBody;
import com.github.category.web.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;


    public QuestionEntity createQuestion(String content, String userTimeZone) {
        // 사용자의 시간대에 맞는 ZonedDateTime 생성
        ZonedDateTime userZonedDateTime = ZonedDateTime.now(ZoneId.of(userTimeZone));

        // 새로운 질문 생성
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(content);
        questionEntity.setCreatedAt(userZonedDateTime); // 사용자의 시간대 반영

        // 질문 저장
        return questionRepository.save(questionEntity);
    }

    public QuestionDTO createQuestion(QuestionBody questionBody) {
        QuestionEntity questionEntity = QuestionMapper.INSTANCE.idAndQuestionBodyToQuestionEntity(null,questionBody);
        QuestionEntity questionCreated = questionRepository.save(questionEntity);
        QuestionDTO questionDTO = QuestionMapper.INSTANCE.questionEntityToQuestionDTO(questionCreated);
        return questionDTO;
    }


    //전체 질문 조회
    public List<QuestionDTO> getAllQuestions() {
        List<QuestionEntity> questionEntities = questionRepository.findAll();
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
