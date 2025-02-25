package com.github.category.service.mapper;

import com.github.category.repository.entity.KeywordEntity;
import com.github.category.web.dto.KeywordBody;
import com.github.category.web.dto.KeywordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Mapper
public interface KeywordMapper {
    KeywordMapper INSTANCE = Mappers.getMapper(KeywordMapper.class);

//  @Mapping(target = "createdAt", expression = "java(getZonedDateTime(userTimeZone))")
  @Mapping(target = "keyword", source = "keywordBody.keyword")
   KeywordEntity idAndKeywordBodyToKeywordEntity(Integer keywordId, KeywordBody keywordBody);


//    @Mapping(target="categoryEntity.categoryId", source="keywordBody.categoryId")

    @Mapping(source="categoryEntity.categoryId", target="categoryId")
    KeywordDTO keywordEntityToKeywordDTO(KeywordEntity keywordEntity);
    @Mapping(source="categoryEntity.categoryId", target="categoryId")
    List<KeywordDTO> keywordEntitiesToKeywordDTOs(List<KeywordEntity> keywordEntities);

//    default ZonedDateTime getZonedDateTime(String userTimeZone) {
//        return ZonedDateTime.now(ZoneId.of(userTimeZone)); // 사용자 시간대에 맞는 ZonedDateTime 반환
//    }
}