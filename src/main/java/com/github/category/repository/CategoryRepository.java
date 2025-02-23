package com.github.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findByName(String name);

    boolean existsByName(String name);
}
