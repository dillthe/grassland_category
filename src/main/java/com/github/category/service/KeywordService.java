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
                .orElseThrow(()->new NotFoundException("Keywords are not found with CategoryId: " + categoryId));

        List<KeywordEntity> keywordEntities = keywordRepository.findAllByCategoryEntity(categoryEntity);
        List<KeywordDTO> keywordDTOs = KeywordMapper.INSTANCE.keywordEntitiesToKeywordDTOs(keywordEntities);
        return keywordDTOs;
    }

    // 키워드 추가
    public KeywordDTO createKeyword(int categoryId, KeywordBody keywordBody) {
        boolean keywordExists=keywordRepository.existsByCategoryEntityAndKeyword(categoryId, keywordBody.getKeyword());
            if(keywordExists){
                throw new NotAcceptException("Keyword: " + keywordBody.getKeyword()+" is already added.");
            }
        KeywordEntity keywordEntity = KeywordMapper.INSTANCE.idAndKeywordBodyToKeywordEntity(null,keywordBody);
        KeywordEntity savedKeyword = keywordRepository.save(keywordEntity);
        KeywordDTO keywordDTO = KeywordMapper.INSTANCE.keywordEntityToKeywordDTO(savedKeyword);
        return keywordDTO;
    }


    // 키워드 삭제
    public boolean deleteKeyword(Long categoryId, String keyword) {
        Optional<Keyword> keywordToDelete = keywordRepository.findByCategoryIdAndKeyword(categoryId, keyword);
        if (keywordToDelete.isPresent()) {
            keywordRepository.delete(keywordToDelete.get());
            return true;
        } else {
            return false;
        }
    }


}

