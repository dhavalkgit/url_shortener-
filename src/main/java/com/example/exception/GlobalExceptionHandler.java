package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(URLException.class)
    public ResponseEntity<?> invalidURLException(URLException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("issue", ex.getCause());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);

    }

    @ExceptionHandler(AliasException.class)
    public ResponseEntity<?> invalidAliasException(AliasException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("issue", ex.getCause());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ShortUrlException.class)
    public ResponseEntity<?> invalidAliasException(ShortUrlException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("issue", ex.getCause());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(UserAlreadyException.class)
    public ResponseEntity<?> userPresentException(UserAlreadyException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("issue", ex.getCause());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> userPresentException(ResourceNotFound ex){
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("issue", ex.getCause());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
