package com.example.newsFeed.global.exception;

public class CustomException extends RuntimeException {

    private final Errors errors;

    public CustomException(Errors errors) {
        super(errors.getMessage());
        this.errors = errors;
    }

    public CustomException(Errors errors, String message) {
        super(message);
        this.errors = errors;
    }
}
