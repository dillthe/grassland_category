package com.github.category.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {
    @Value("${OPENAI_API_KEY}")
    private String openAiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String categorizeQuestion(String question, List<String> categories) {
        RestTemplate restTemplate = new RestTemplate();
        String categoriesJson = String.join(", ", categories);
        String prompt = "질문과 연관된 카테고리 명이나 태그 명을 이름으로 간단하게 반환해 "
                +  " **질문:** \"" + question + "\"\n\n" +
                " **정확한 카테고리 이름만 한 단어로 반환하세요.** (예: '인간관계', '기도와 신앙생활')\n" +
                " '기타'는 가급적 사용하지 마세요. 꼭 필요할 경우에만 사용하세요.";
//

//        String prompt = "다음 카테고리 중 질문과 가장 연관성이 높은 것을 반환하세요! " +
//                "["+categoriesJson + "].\n\n"+
//                "카테고리 설명 : **\n" +
//                "[" + categoriesJson + "].\n\n" +
//                "💡 **카테고리 설명:**\n" +
//                "- '하나님의 계획과 인도하심': 하나님의 뜻, 계획, 삶의 인도하심에 대한 질문\n" +
//                "- '인생, 삶과 신앙': 신앙과 삶의 조화, 인생의 방향성에 대한 고민\n" +
//                "- '기도와 신앙생활': 기도 방법, 영적 성장, 신앙생활 실천에 대한 질문\n" +
//                "- '성경 및 신학적 질문': 성경 해석, 신학적 개념, 교리 관련 질문\n" +
//                "- '윤리와 도덕': 도덕적 가치 판단, 기독교 윤리와 관련된 문제\n" +
//                "- '사랑과 가족': 부부 관계, 연애, 가족 갈등, 결혼 관련 고민\n" +
//                "- '인간관계': 친구, 직장, 사회적 관계에서의 갈등과 해결 방법\n" +
//                "- '부와 명예, 우상과 미신': 돈, 성공, 명예, 미신과 기독교적 관점에 대한 질문\n" +
//                "- '예배와 교회생활': 교회 내 신앙생활, 예배, 봉사 관련 질문\n" +
//                "- '개인적 고민과 상담': 개인적인 어려움, 정신적·심리적 상담이 필요한 고민\n\n" +
//                "**질문이 여러 카테고리에 해당할 경우, 가장 직접적인 관련이 있는 카테고리를 선택하세요.**\n\n" +
//                " **질문:** \"" + question + "\"\n\n" +
//                " **정확한 카테고리 이름만 한 단어로 반환하세요.** (예: '인간관계', '기도와 신앙생활')\n" +
//                " '기타'는 가급적 사용하지 마세요. 꼭 필요할 경우에만 사용하세요.";

        Map<String, Object> request = Map.of(
                "model","gpt-4o-mini",/*"gpt-4",*/
                "messages",
                new Object[]{Map.of("role", "system",
                        "content", "You are a helpful AI assistant."),
                        Map.of("role", "user", "content", prompt)},
                "max_tokens", 10
        );



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, Map.class);

        return parseResponse(response.getBody());
    }

    private String parseResponse(Map<String, Object> responseBody) {
        if (responseBody == null || !responseBody.containsKey("choices")) {
            return "General";
        }
        return ((Map<String, String>) ((Map<String, Object>) ((java.util.List<?>) responseBody.get("choices")).get(0)).get("message")).get("content").trim();
    }
}
