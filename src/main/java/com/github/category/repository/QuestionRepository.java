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

}
