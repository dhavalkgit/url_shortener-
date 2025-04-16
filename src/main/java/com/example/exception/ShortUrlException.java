package com.example.exception;

public class ShortUrlException extends RuntimeException{
    public ShortUrlException(String message) {
        super(message);
    }
}
