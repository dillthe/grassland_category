package com.github.category.repository;

import com.github.category.repository.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    Optional<TagEntity> findByTag(String tagName);

//    @Query("SELECT t FROM TagEntity t JOIN FETCH t.questions WHERE t.tagId = :tagId")
//    Optional<TagEntity> findTagWithQuestions(@Param("tagId") int tagId);

}
