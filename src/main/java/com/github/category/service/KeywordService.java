package com.github.category.service;

import com.github.category.repository.CategoryRepository;
import com.github.category.repository.KeywordRepository;
import com.github.category.repository.entity.CategoryEntity;
import com.github.category.repository.entity.KeywordEntity;
import com.github.category.service.exceptions.NotAcceptException;
import com.github.category.service.exceptions.NotFoundException;
import com.github.category.service.mapper.CategoryMapper;
import com.github.category.service.mapper.KeywordMapper;
import com.github.category.web.dto.KeywordBody;
import com.github.category.web.dto.KeywordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;

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

        KeywordEntity existingKeyword = (KeywordEntity) keywordRepository.findByKeywordAndCategoryEntity(keywordBody.getKeyword(), categoryEntity)
                .orElseThrow(() -> new NotFoundException("Keyword doesn't exist in the specified category"));
        KeywordRepository.deleteByKeyword(existingKeyword);
        return "Keyword Id: " + existingKeyword.getKeywordId() + ", Keyword Name: " + existingKeyword.getKeyword() + "is deleted.";
        }

    // 키워드 삭제(키워드 Id로 조회)
    public String deleteKeywordById(int categoryId, int keywordId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + categoryId + " doesn't exist"));

        KeywordEntity existingKeyword = (KeywordEntity) keywordRepository.findByKeywordIdAndCategoryEntity(keywordId,categoryEntity)
            .orElseThrow(() -> new NotFoundException("Keyword doesn't exist in the specified category"));
        KeywordRepository.deleteById(existingKeyword);
        return "Keyword Id: " + existingKeyword.getKeywordId() + ", Keyword Name: " + existingKeyword.getKeyword() + "is deleted.";
    }
}



