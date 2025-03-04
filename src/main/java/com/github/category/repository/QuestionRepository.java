package com.github.category.repository;

import com.github.category.repository.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.QuestionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
    List<QuestionEntity> findByCategoryEntity(CategoryEntity categoryEntity);


    // 2. @Query 기반 쿼리
    @Query("SELECT q FROM QuestionEntity q JOIN q.tags t WHERE t.tagId = :tagId")
    Set<QuestionEntity> findByTagId(@Param("tagId") int tagId);
}
