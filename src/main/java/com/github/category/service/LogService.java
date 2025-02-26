package com.github.category.service;

import com.github.category.repository.LogRepository;
import com.github.category.repository.entity.LogEntity;
import com.github.category.repository.entity.QuestionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
//
//    private final LogRepository logRepository;
//

    //질문이 어떤 키워드로 매칭됐는지 파악하기 + 오류가 있다면 그걸 수정하는 코드도 넣기..
//    public void logCategoryMatch(QuestionEntity questionEntity, String categoryName,
//                                 List<String> matchedKeywords, String logLevel, String message) {
//        LogEntity log = new Log();
//        log.setQuestionEntity(questionEntity);
//        log.setCategoryName(categoryName);
//        log.setMatchedKeywords(String.join(", ", matchedKeywords));
//        log.setLogLevel(logLevel);
//        log.setMessage(message);
//
//        logRepository.save(log);
//    }

}
