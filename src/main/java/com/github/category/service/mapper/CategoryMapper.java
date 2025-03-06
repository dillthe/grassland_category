package com.github.category.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.web.dto.CategoryBody;
import com.github.category.web.dto.CategoryDTO;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target="name", source="categoryBody.name")
    CategoryEntity idAndCategoryBodyToCategoryEntity(Integer id, CategoryBody categoryBody);

    CategoryDTO categoryEntityToCategoryDTO(CategoryEntity categoryEntity);

    List<CategoryDTO> categoryEntitiesToCategoryDTOs(List<CategoryEntity> categoryEntities);
}
