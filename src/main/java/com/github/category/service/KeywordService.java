package com.github.category.service;

import com.github.category.repository.CategoryRepository;
import com.github.category.repository.KeywordRepository;
import com.github.category.repository.TagRepository;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.repository.entity.KeywordEntity;
import com.github.category.repository.entity.QuestionEntity;
import com.github.category.repository.entity.TagEntity;
import com.github.category.service.exceptions.NotAcceptException;
import com.github.category.service.exceptions.NotFoundException;
import com.github.category.service.mapper.CategoryMapper;
import com.github.category.service.mapper.KeywordMapper;
import com.github.category.web.dto.KeywordBody;
import com.github.category.web.dto.KeywordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    // 키워드 전체 조회
    public List<KeywordDTO> getAllKeywords() {
        List<KeywordEntity> keywordEntities = keywordRepository.findAll();
        List<KeywordDTO> keywordDTOs = KeywordMapper.INSTANCE.keywordEntitiesToKeywordDTOs(keywordEntities);
        return keywordDTOs;
    }

    //카테고리별 키워드 조회
    public List<KeywordDTO> getKeywordsByCategory(int categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(()->new NotFoundException("Category with ID " + categoryId + " not found"));

        List<KeywordEntity> keywordEntities = keywordRepository.findAllByCategoryEntity(categoryEntity);
        List<KeywordDTO> keywordDTOs = KeywordMapper.INSTANCE.keywordEntitiesToKeywordDTOs(keywordEntities);
        return keywordDTOs;
    }

    // 키워드 추가
    public KeywordDTO createKeyword(int categoryId, KeywordBody keywordBody) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(()->new NotFoundException("Category with ID " + categoryId + " not found"));

        boolean keywordExists=keywordRepository.existsByCategoryEntityAndKeyword(categoryEntity, keywordBody.getKeyword());
            if(keywordExists){
                throw new NotAcceptException("Keyword: " + keywordBody.getKeyword()+" is already added.");
            }
        KeywordEntity keywordEntity = KeywordMapper.INSTANCE.idAndKeywordBodyToKeywordEntity(null,keywordBody);
        keywordEntity.setCategoryEntity(categoryEntity);
        KeywordEntity savedKeyword = keywordRepository.save(keywordEntity);
        KeywordDTO keywordDTO = KeywordMapper.INSTANCE.keywordEntityToKeywordDTO(savedKeyword);
        return keywordDTO;
    }

    // 키워드 여러개 추가
    public List<KeywordDTO> createKeywords(int categoryId, List<KeywordBody> keywordBodies) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + categoryId + " not found"));

        List<KeywordEntity> savedKeywordList = new ArrayList<>();

        for (KeywordBody keywordBody : keywordBodies) {
            boolean keywordExists = keywordRepository.existsByCategoryEntityAndKeyword(categoryEntity, keywordBody.getKeyword());
            if (keywordExists) {
                throw new NotAcceptException("Keyword: " + keywordBody.getKeyword() + " is already added.");
            }
            KeywordEntity keywordEntity = KeywordMapper.INSTANCE.idAndKeywordBodyToKeywordEntity(null, keywordBody);
            keywordEntity.setCategoryEntity(categoryEntity);
            KeywordEntity savedKeyword = keywordRepository.save(keywordEntity);
            savedKeywordList.add(savedKeyword);

        }List<KeywordDTO> keywordDTOs = KeywordMapper.INSTANCE.keywordEntitiesToKeywordDTOs(savedKeywordList);

        return keywordDTOs;
    }


    // 키워드 삭제(키워드 Keyword로 조회)
    public String deleteKeyword(int categoryId, KeywordBody keywordBody) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + categoryId + " doesn't exist"));

        KeywordEntity existingKeyword =keywordRepository.findByKeywordAndCategoryEntity(keywordBody.getKeyword(), categoryEntity)
                .orElseThrow(() -> new NotFoundException("Keyword doesn't exist in the specified category"));
        KeywordRepository.deleteByKeyword(existingKeyword);
        return "Keyword Id: " + existingKeyword.getKeywordId() + ", Keyword Name: " + existingKeyword.getKeyword() + "is deleted.";
        }

    // 키워드 삭제(키워드 Id로 조회)
    public String deleteKeywordById(int categoryId, int keywordId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + categoryId + " doesn't exist"));

        KeywordEntity existingKeyword =keywordRepository.findByKeywordIdAndCategoryEntity(keywordId,categoryEntity)
            .orElseThrow(() -> new NotFoundException("Keyword doesn't exist in the specified category"));
        KeywordRepository.deleteById(existingKeyword.getKeywordId());
        return "Keyword Id: " + existingKeyword.getKeywordId() + ", Keyword Name: " + existingKeyword.getKeyword() + "is deleted.";
    }

    //키워드 삭제 (해당 카테고리 내 전체 키워드 삭제)
    public String deleteAllKeywordsByCategory(int categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + categoryId + " doesn't exist"));

        List<KeywordEntity> existingKeywords = keywordRepository.findAllByCategoryEntity(categoryEntity);
        if (existingKeywords.isEmpty()) {
            throw new NotFoundException("No keywords found in the specified category");
        }
        keywordRepository.deleteAll(existingKeywords);
        return "Deleted " + existingKeywords.size() + " keywords in category: " + categoryEntity.getName();
    }

    @Transactional
    public Set<TagEntity> createTagsFromMatchedKeywords(List<String> matchedKeywords, QuestionEntity questionEntity) {
        Set<TagEntity> tagEntities = new HashSet<>();

        for (String keyword : matchedKeywords) {
            TagEntity tagEntity = tagRepository.findByTag(keyword)
                    .orElseGet(() -> {
                        TagEntity newTag = new TagEntity();
                        newTag.setTag(keyword);
                        return tagRepository.save(newTag);
                    });

            tagEntities.add(tagEntity);
            tagEntity.getQuestions().add(questionEntity);
        }

        return tagEntities;
    }
}



