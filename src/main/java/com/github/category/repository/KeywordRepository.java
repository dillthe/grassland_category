package com.github.category.repository;

import com.github.category.repository.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.KeywordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Integer> {
    List<KeywordEntity> findAllByCategoryEntity(CategoryEntity categoryEntity);

    boolean existsByKeyword(String keyword);

    boolean existsByCategoryEntityAndKeyword(int categoryId, String keyword);
}

