package com.example.exception;

public class AliasException extends RuntimeException{
    public AliasException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliasException(String message) {
        super(message);
    }
}
