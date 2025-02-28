package com.github.category.service;


import com.github.category.repository.CategoryRepository;
import com.github.category.repository.KeywordRepository;
import com.github.category.repository.QuestionRepository;
import com.github.category.repository.TagRepository;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.repository.entity.KeywordEntity;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.repository.entity.TagEntity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
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


    @Transactional
    public String determineCategory(String questionText){
        List<String> categories = categoryRepository.findAll()
                .stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());

        String category = simpleKeywordMatching(questionText);

        if (category == null) {
            category = openAIService.categorizeQuestion(questionText,categories);
//            category = findClosestCategory(category, categories);
            }
            return category;}

    public String simpleKeywordMatching(String questionText) {
        List<KeywordEntity> keywords = keywordRepository.findAll();

        for (KeywordEntity keyword : keywords) {
            if (questionText.contains(keyword.getKeyword())) {
                String categoryName = keyword.getCategoryEntity().getName();
//                saveTag(questionText, String.valueOf(keyword));
                return categoryName;
            }
        }

        return null;
    }

//    private String findClosestCategory(String input, List<String> categories) {
//        String closestCategory = categories.stream()
//                .min((c1, c2) -> Integer.compare(
//                        levenshtein.apply(input, c1),
//                        levenshtein.apply(input, c2)))
//                .orElse(null);
//        return closestCategory != null ? closestCategory : input;
//    }
//
//    private void saveTag(String questionText, String category) {
////        QuestionEntity question = questionRepository.findByQuestion(questionText)
////                .orElseThrow(() -> new NotFoundException("Question is not found"));
//
//        // 기존 태그가 있는지 확인
//        TagEntity tag = tagRepository.findByTag(category)
//                .orElseGet(() -> {
//                    TagEntity newTag = new TagEntity();
//                    newTag.setTag(category);
//                    return tagRepository.save(newTag);
//                });
////
//        QuestionEntity question = questionRepository.findByQuestion(questionText)
//                .orElseGet(() -> {
//                    // Question이 없으면 새로 생성
//                    QuestionEntity newQuestion = new QuestionEntity();
//                    newQuestion.setQuestion(questionText);
//                    return questionRepository.save(newQuestion); // 질문을 저장
//                });
//
//        // 질문에 태그 추가
//        question.getTags().add(tag);
//        tag.getQuestions().add(question);
//
//        // 연결 후 업데이트된 QuestionEntity와 TagEntity를 저장
//        questionRepository.save(question);  // Question이 태그와 연결된 상태로 저장됨
//        tagRepository.save(tag);
//    }
}


