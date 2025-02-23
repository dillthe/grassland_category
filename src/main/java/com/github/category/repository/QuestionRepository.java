package com.github.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
}
