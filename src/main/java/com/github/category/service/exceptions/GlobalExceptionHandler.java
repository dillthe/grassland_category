package com.github.category.service.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotAcceptException.class)
    public ResponseEntity<Map<String, Object>> handleNotAcceptableException(NotAcceptException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_ACCEPTABLE.value());
        response.put("error", "Not Acceptable");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }
}