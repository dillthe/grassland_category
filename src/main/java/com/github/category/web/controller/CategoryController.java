package com.github.category.web.controller;


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
        System.out.println("📌 Received request: " + categoryBody);
        String category = categoryService.createCategory(categoryBody);
        return "CategoryId:" + category + "was created";
    }

//    @Operation(summary="Create many categories")
//    @PostMapping("/api/category/batch")
//    public String createCategories(@RequestBody List<CategoryBody> categoryBodies){
//        List<String> categories = categoryService.createCategories(categoryBodies);
//        return "Categories:" + categories + "were created";
//    }

//    @Operation(summary="Get all categories")
    @GetMapping
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

//    @Operation(summary="Delete a category")
    @DeleteMapping
    public String deleteCategory(@PathVariable int categoryId){
        String deletion = categoryService.deleteCategory(categoryId);
        return deletion;
    }

}
