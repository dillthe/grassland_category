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
    private final TagRepository tagRepository;
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
            System.out.println("Determined category: " + categoryName);  // 로그 추가

            if (categoryName == null || categoryName.trim().isEmpty()) {
                categoryName = "기타";  // 기본값 설정
            }
            CategoryEntity categoryEntity = categoryRepository.findByName(categoryName)
                    .orElse(null);  // 카테고리가 없다면 null 반환

            //카테고리가 존재하지 않으면 DB에 저장하지 않고 그대로 사용 (null로 두기, 대신 tag로 등록함)
            if (categoryEntity == null) {
                // 카테고리가 없으므로 DB에 저장하지 않음
                System.out.println("category: null, tag: " + categoryName);
            } else {
                // 카테고리가 DB에 존재하면, QuestionEntity에 해당 카테고리 설정
                questionEntity.setCategoryEntity(categoryEntity);
            }

        String tagName = categoryName;
        TagEntity tagEntity = tagRepository.findByTag(categoryName)
                    .orElseGet(() -> {
                        // Tag가 없으면 새로 생성
                        TagEntity newTag = new TagEntity();
                        newTag.setTag(tagName);
                        tagRepository.save(newTag); // 새 태그 저장
                        return newTag; // 저장된 새 태그 반환
                    });
            questionEntity.getTags().add(tagEntity);
            tagEntity.getQuestions().add(questionEntity);


            QuestionEntity questionCreated = questionRepository.save(questionEntity);
            tagRepository.save(tagEntity);
            QuestionDTO questionDTO = QuestionMapper.INSTANCE.questionEntityToQuestionDTO(questionCreated);
            return "Question is created: " + questionDTO.getQuestion() + ", Category: " + questionDTO.getCategoryName() + ", Tag: " + tagName ;
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
