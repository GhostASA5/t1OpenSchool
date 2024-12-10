package com.project.t1_openSchool.controllers.exception;

import com.project.t1_openSchool.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> userNotFound(RuntimeException ex) {
        log.error("Error when trying to get a user.", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getLocalizedMessage()));
    }
}
