package com.backend.application.exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

    public AuthException(HttpStatus httpStatus) {
        super(String.valueOf(httpStatus));
    }
}
