package com.github.category.web.controller;

import com.github.category.service.CsvFileService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class CsvController {

    @Autowired
    private CsvFileService csvFileService;

    @GetMapping("/read-csv")
    public List<Map<String, String>> readCsv() {
        try {
            List<String[]> rows = csvFileService.readCsvFile();

            // 첫 번째 행은 헤더로 사용
            String[] headers = rows.get(0);

            List<Map<String, String>> result = new ArrayList<>();

            // 두 번째 행부터 실제 데이터
            for (int i = 1; i < rows.size(); i++) {
                Map<String, String> rowMap = new LinkedHashMap<>();
                String[] row = rows.get(i);

                for (int j = 0; j < headers.length; j++) {
                    rowMap.put(headers[j], row[j]);
                }

                result.add(rowMap);
            }

            return result;  // CSV 데이터를 JSON 형태로 반환
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return Collections.emptyList();  // 오류 발생 시 빈 리스트 반환
        }
    }
//    @GetMapping("/read-csv")
//    public String readCsv() {
//        try {
//            csvFileService.readCsvFile();
//            return "CSV 파일을 성공적으로 읽었습니다!";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "CSV 파일 읽기 실패!";
//        }
//    }
}
