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

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public String createCategory(CategoryBody categoryBody) {
//        boolean categoryExists = categoryRepository.findByName(categoryBody.getName());
//        if(categoryExists){
//            throw new NotAcceptException("Category with the same name: " + categoryBody.getName() + " already exists.");
//        }

        CategoryEntity categoryEntity = CategoryMapper.INSTANCE.idAndCategoryBodyToCategoryEntity(null,categoryBody);
        CategoryEntity categoryCreated = categoryRepository.save(categoryEntity);
        return categoryCreated.getName();
    }
//
//    public List<String> createCategories(List<CategoryBody> categoryBodies) {
//        List<String> categories = categoryBodies.stream()
//                .map(categoryBody->{
//                    if(categoryRepository.existsByName(categoryBody.getName())){
//                        throw new NotAcceptException("Category with the same name already exists: " + categoryBody.getName());
//                    }
//                    return createCategory(categoryBody);
//                })
//                .collect(Collectors.toList());
//        return categories;
//    }


    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = CategoryMapper.INSTANCE.categoryEntitiesToCategoryDTOs(categoryEntities);
        return categoryDTOs;
    }

    public String deleteCategory(int categoryId) {
        CategoryEntity existingCategory = categoryRepository.findById(categoryId)
                        .orElseThrow(()->new NotAcceptException("Category doesn't exist"));
        categoryRepository.deleteById(categoryId);
        return "Category Id: " + categoryId + ", Category Name: " + existingCategory.getName() + "is deleted.";
    }



}
