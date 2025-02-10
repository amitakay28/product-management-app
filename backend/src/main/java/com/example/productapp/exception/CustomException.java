package com.example.productapp.exception;


/**
 * Custom exception message for the API error response.
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
