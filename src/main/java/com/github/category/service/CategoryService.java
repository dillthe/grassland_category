package com.github.category.service;


import com.github.category.repository.CategoryRepository;
import com.github.category.repository.KeywordRepository;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.repository.entity.KeywordEntity;
import com.github.category.service.exceptions.ConflictException;
import com.github.category.service.exceptions.InvalidValueException;
import com.github.category.service.exceptions.NotAcceptException;
import com.github.category.service.exceptions.NotFoundException;
import com.github.category.service.mapper.CategoryMapper;
import com.github.category.web.dto.CategoryBody;
import com.github.category.web.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final OpenAIService openAIService;
    private final LevenshteinDistance levenshtein = new LevenshteinDistance();


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

        if (Objects.equals(existingCategory.getName(), categoryBody.getName())) {
            throw new ConflictException("The new category name is the same as the current category name: " + categoryBody.getName());
        }

        existingCategory.setName(categoryBody.getName());
        CategoryEntity updatedCategory = categoryRepository.save(existingCategory);
        CategoryDTO categoryDTO = CategoryMapper.INSTANCE.categoryEntityToCategoryDTO(updatedCategory);

        return "Category Id: " + categoryId + ", Category Name is updated to " + categoryDTO.getName();

    }

    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = CategoryMapper.INSTANCE.categoryEntitiesToCategoryDTOs(categoryEntities);
        return categoryDTOs;
    }

//    전체 카테고리 및 카테고리에 포함된 전체 키워드 조회
    @Transactional
    public List<CategoryEntity> getCategoriesWithKeywords() {
        return categoryRepository.findAllWithKeywords();
    }




    public String deleteCategory(int categoryId) {
        CategoryEntity existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category doesn't exist"));
        categoryRepository.deleteById(categoryId);
        return "Category Id: " + categoryId + ", Category Name: " + existingCategory.getName() + "is deleted.";
    }

    public String determineCategory(String questionText){
        List<String> categories = categoryRepository.findAll()
                .stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());

        String category = simpleKeywordMatching(questionText);
        if (category == null) {
            category = openAIService.categorizeQuestion(questionText,categories);
            category = findClosestCategory(category, categories); // 유사한 카테고리 찾기
        }
        return category;
    }

    public String simpleKeywordMatching(String questionText) {
        List<KeywordEntity> keywords = keywordRepository.findAll();

        for (KeywordEntity keyword : keywords) {
            if (questionText.contains(keyword.getKeyword())) {
                return keyword.getCategoryEntity().getName();
            }
        }

        return null; // 매칭되는 키워드가 없으면 기본 카테고리
    }

    private String findClosestCategory(String input, List<String> categories) {
        return categories.stream()
                .min((c1, c2) -> Integer.compare(
                        levenshtein.apply(input, c1),
                        levenshtein.apply(input, c2)))
                .orElse("General");
    }
}


