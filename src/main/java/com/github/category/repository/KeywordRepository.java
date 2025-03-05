package com.github.category.repository;

import com.github.category.repository.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.KeywordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Integer> {

    List<KeywordEntity> findAllByCategoryEntity(CategoryEntity categoryEntity);


    boolean existsByCategoryEntityAndKeyword(CategoryEntity categoryEntity, String keyword);


    Optional<KeywordEntity> findByKeywordAndCategoryEntity(String keyword, CategoryEntity categoryEntity);

    Optional<KeywordEntity> findByKeywordIdAndCategoryEntity(int keywordId, CategoryEntity categoryEntity);


    List<KeywordEntity> findByKeyword(String firstMatchedKeyword);
}

