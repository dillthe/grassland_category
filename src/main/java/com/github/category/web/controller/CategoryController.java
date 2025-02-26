package com.github.category.web.controller;


import com.github.category.repository.entity.CategoryEntity;
import com.github.category.web.dto.CategoryBody;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.github.category.service.CategoryService;
import com.github.category.web.dto.CategoryDTO;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary="Create a new category")
    @PostMapping
    public String createCategory(@RequestBody CategoryBody categoryBody){
        String category = categoryService.createCategory(categoryBody);
        return "Category:" + category + "was created";
    }

    @Operation(summary="Create many categories")
    @PostMapping("/batch")
    public String createCategories(@RequestBody List<CategoryBody> categoryBodies){
        List<String> categories = categoryService.createCategories(categoryBodies);
        return "Categories:" + categories + "were created";
    }

    @Operation(summary="Update a category information")
    @PutMapping("/{categoryId}")
    public String updateCategory(@PathVariable int categoryId, @RequestBody CategoryBody categoryBody) {
        String updatedCategory = categoryService.updateCategory(categoryId, categoryBody);
        return updatedCategory;
    }


    @Operation(summary="Get all categories")
    @GetMapping
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }


    @Operation(summary="Get all categories with keywords")
    @GetMapping("/with-keywords")
    public List<CategoryEntity> getCategoriesWithKeywords() {
        List<CategoryEntity> categories = categoryService.getCategoriesWithKeywords();
        return categories;
    }


    @Operation(summary="Delete a category")
    @DeleteMapping("/{categoryId}")
    public String deleteCategory(@PathVariable int categoryId){
        String deletion = categoryService.deleteCategory(categoryId);
        return deletion;
    }

}
