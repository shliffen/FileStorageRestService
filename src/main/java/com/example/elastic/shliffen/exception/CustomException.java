package com.example.elastic.shliffen.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom class that I made for handling and processing exceptions by RestExceptionHandler class
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private HttpStatus status;

    public CustomException() {

    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
