//package com.github.category.web.controller;
//
//import com.github.category.service.LogService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/logs")
//@RequiredArgsConstructor
//public class LogController {
//    private final LogService logService;
//
//    // 로그 전체 조회
//    @GetMapping
//    public List<LogDTO> getAllLogs() {
//        return logService.getAllLogs();
//    }
//
//    // 로그 단건 조회
//    @GetMapping("/{id}")
//    public LogDTO getLog(@PathVariable Long id) {
//        return logService.getLogById(id)
//                .orElseThrow(() -> new NotFoundException("Log not found"));
//    }
//
//    // 로그 추가
//    @PostMapping
//    public LogDTO createLog(@RequestBody LogDTO logDTO) {
//        return logService.createLog(logDTO);
//    }
//
//    // 로그 수정
//    @PutMapping("/{id}")
//    public LogDTO updateLog(@PathVariable Long id, @RequestBody LogDTO logDTO) {
//        return logService.updateLog(id, logDTO)
//                .orElseThrow(() -> new NotFoundException("Log not found"));
//    }
//
//    // 로그 삭제
//    @DeleteMapping("/{id}")
//    public void deleteLog(@PathVariable Long id) {
//        boolean deleted = logService.deleteLog(id);
//        if (!deleted) {
//            throw new NotFoundException("Log not found");
//        }
//    }
//}
