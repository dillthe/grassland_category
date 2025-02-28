package com.github.category.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int tagId;

    @Column(name = "tag", nullable = false)
    private String tag;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "question_tag", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "tag_id"),  // TagEntity에서 참조하는 컬럼
            inverseJoinColumns = @JoinColumn(name = "question_id") // QuestionEntity에서 참조하는 컬럼
    )
    private Set<QuestionEntity> questions = new HashSet<>();

}