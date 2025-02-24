package com.github.category.service.mapper;

import com.github.category.repository.entity.KeywordEntity;
import com.github.category.web.dto.KeywordBody;
import com.github.category.web.dto.KeywordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface KeywordMapper {
    KeywordMapper INSTANCE = Mappers.getMapper(KeywordMapper.class);

//  @Mapping(target = "createdTimeAt", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target="categoryEntity.categoryId", source="keywordBody.categoryId")
    @Mapping(target = "keyword", source = "keywordBody.keyword")
    KeywordEntity idAndKeywordBodyToKeywordEntity(Integer keywordId, KeywordBody keywordBody);


    @Mapping(source="categoryEntity.categoryId", target="categoryId")
    KeywordDTO keywordEntityToKeywordDTO(KeywordEntity keywordEntity);
    @Mapping(source="categoryEntity.categoryId", target="categoryId")

    List<KeywordDTO> keywordEntitiesToKeywordDTOs(List<KeywordEntity> keywordEntities);
}