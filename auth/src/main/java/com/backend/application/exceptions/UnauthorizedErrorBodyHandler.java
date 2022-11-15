package com.backend.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnauthorizedErrorBodyHandler {

    @ExceptionHandler
    public ResponseEntity<UnauthorizedErrorBody> handleException(AuthException exception) {
        UnauthorizedErrorBody data = new UnauthorizedErrorBody();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);
    }
}
