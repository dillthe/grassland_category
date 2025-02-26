package com.github.category.repository;

import com.github.category.repository.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.LogEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Integer> {
    List<LogEntity> findByQuestionEntity(QuestionEntity questionEntity);
}
