package com.github.category.service;


import com.github.category.repository.CategoryRepository;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.service.exceptions.NotAcceptException;
import com.github.category.service.mapper.CategoryMapper;
import com.github.category.web.dto.CategoryBody;
import com.github.category.web.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public String createCategory(CategoryBody categoryBody) {
        boolean categoryExists = categoryRepository.existsByName(categoryBody.getName());
        if (categoryExists) {
            throw new NotAcceptException("Category with the same name: " + categoryBody.getName() + " already exists.");
        }

        CategoryEntity categoryEntity = CategoryMapper.INSTANCE.idAndCategoryBodyToCategoryEntity(null, categoryBody);
        CategoryEntity categoryCreated = categoryRepository.save(categoryEntity);
        return categoryCreated.getName();
    }

    public List<String> createCategories(List<CategoryBody> categoryBodies) {
        for (CategoryBody categoryBody : categoryBodies) {
            if (categoryRepository.existsByName(categoryBody.getName())) {
                throw new NotAcceptException("Category with the same name already exists: " + categoryBody.getName());
            }
        }
        List<CategoryEntity> categoryEntities = categoryBodies.stream()
                .map(categoryBody -> CategoryMapper.INSTANCE.idAndCategoryBodyToCategoryEntity(null, categoryBody))
                .collect(Collectors.toList());
        List<CategoryEntity> savedCategories = categoryRepository.saveAll(categoryEntities);

        return savedCategories.stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());
    }

    public String updateCategory(int categoryId, CategoryBody categoryBody) {
        CategoryEntity existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotAcceptException("Category doesn't exist"));

        existingCategory.setName(categoryBody.getName());
        CategoryEntity updatedCategory = categoryRepository.save(existingCategory);
        CategoryDTO categoryDTO = CategoryMapper.INSTANCE.categoryEntityToCategoryDTO(updatedCategory);

        return "Category Id: " + categoryId + ", Category Name: " + categoryDTO.getName() + "is deleted.";

    }

    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = CategoryMapper.INSTANCE.categoryEntitiesToCategoryDTOs(categoryEntities);
        return categoryDTOs;
    }

    public String deleteCategory(int categoryId) {
        CategoryEntity existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotAcceptException("Category doesn't exist"));
        categoryRepository.deleteById(categoryId);
        return "Category Id: " + categoryId + ", Category Name: " + existingCategory.getName() + "is deleted.";
    }


    //먼저 키워드를 통해 분류할 수 있게 하고, AI를 통해 2차로 카테고리를 분류할 수 있게 하는 코드로 짜보기
    public CategoryEntity classifyCategory(String content) {

        if (content.contains("기술")) {
            return categoryRepository.findByName("기술")
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        } else if (content.contains("생활")) {
            return categoryRepository.findByName("생활")
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        }
        // 기본적으로 다른 카테고리로 분류하거나 기타 카테고리를 반환
        return categoryRepository.findByName("기타")
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
}

}
