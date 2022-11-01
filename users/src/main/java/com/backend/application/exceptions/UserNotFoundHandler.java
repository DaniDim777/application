package com.backend.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserNotFoundHandler {

    @ExceptionHandler
    public ResponseEntity<UserNotFoundException> handleException(UserException exception) {
        UserNotFoundException data = new UserNotFoundException();
        data.setApplication_info(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
