package com.github.category.service.mapper;

import com.github.category.repository.entity.QuestionEntity;
import com.github.category.web.dto.QuestionBody;
import com.github.category.web.dto.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "question", source = "questionBody.question")
//    @Mapping(target = "createdTimeAt", expression = "java(java.time.ZonedDateTime.now())")
    QuestionEntity idAndQuestionBodyToQuestionEntity(Integer id, QuestionBody questionBody);

    @Mapping(target="categoryName", source = "categoryEntity.name")
    QuestionDTO questionEntityToQuestionDTO(QuestionEntity questionEntity);

    @Mapping(target="categoryName", source = "categoryEntity.name")
    List<QuestionDTO> questionEntitiesToQuestionDTOs(List<QuestionEntity> questionEntities);
}