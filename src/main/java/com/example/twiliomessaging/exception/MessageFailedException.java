package com.example.twiliomessaging.exception;

public class MessageFailedException extends RuntimeException {

    public MessageFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
