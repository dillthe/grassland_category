package com.github.category.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Question")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int questionId;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

//    @Column(name = "created_at", updatable = false)
//    private ZonedDateTime createdAt = ZonedDateTime.now();

    // UTC 기준으로 저장
    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();


    @ManyToMany(mappedBy = "questions")
    private Set<TagEntity> tags = new HashSet<>();
}