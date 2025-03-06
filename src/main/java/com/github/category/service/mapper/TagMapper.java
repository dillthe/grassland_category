package com.github.category.service.mapper;

import com.github.category.repository.entity.QuestionEntity;
import com.github.category.repository.entity.TagEntity;
import com.github.category.web.dto.QuestionBody;
import com.github.category.web.dto.QuestionDTO;
import com.github.category.web.dto.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    @Mapping(target = "questionCount", expression = "java(tag.getQuestions().size())")
    TagDTO tagEntityToTagDTO(TagEntity tag);

    List<TagDTO> tagEntitiesToTagDTOs(List<TagEntity> tagEntities);

    default String tagEntityToString(TagEntity tagEntity) {
        return tagEntity != null ? tagEntity.getTag() : null;
    }
}