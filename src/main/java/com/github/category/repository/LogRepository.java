package com.github.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.LogEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Integer> {
}
