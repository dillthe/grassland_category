package com.github.category.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class CsvFileService {


    public List<String[]> readCsvFile() throws IOException, CsvException {
        String filePath = "C:/Users/seulg/Desktop/question.csv";  // 바탕화면에 있는 파일 경로

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();  // CSV 파일 내용 읽기
        }
    }
//    public void readCsvFile() throws IOException {
//        // 바탕화면 경로를 사용
//        String filePath = "C:/Users/seulg/Desktop/question.csv";
//
//        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
//            List<String[]> rows = reader.readAll();
//
//
//            for (String[] row : rows) {
//                // CSV 파일의 각 행(row)을 처리
//                System.out.println("Row data: ");
//                for (String cell : row) {
//                    System.out.print(cell + " ");
//                }
//                System.out.println();
//            }
//        } catch (CsvException | IOException e) {
//            e.printStackTrace();  // 예외 발생 시 스택 트레이스를 출력
//        }
//    }
}
