package com.linss.gshop.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleJwtException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}