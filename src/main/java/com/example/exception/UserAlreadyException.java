package com.example.exception;

public class UserAlreadyException extends RuntimeException{
    public UserAlreadyException(String message) {
        super(message);
    }
}
