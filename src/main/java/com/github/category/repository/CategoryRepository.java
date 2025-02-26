package com.github.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.category.repository.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findByName(String name);

    boolean existsByName(String name);

    Optional<CategoryEntity> findById(int categoryId);

    @Query("SELECT c FROM CategoryEntity c JOIN FETCH c.keywords")
    List<CategoryEntity> findAllWithKeywords();
}
