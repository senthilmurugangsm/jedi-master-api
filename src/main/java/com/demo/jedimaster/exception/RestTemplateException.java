package com.demo.jedimaster.exception;

import org.springframework.http.HttpStatus;

public class RestTemplateException extends RuntimeException {
    private final HttpStatus httpStatus;

    public RestTemplateException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
