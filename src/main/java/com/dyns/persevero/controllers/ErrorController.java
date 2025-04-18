package com.dyns.persevero.controllers;

import com.dyns.persevero.domain.dto.ErrorDto;
import com.dyns.persevero.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.error("Caught ResourceNotFoundException ", exception);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("The requested resource was not found.")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error("Caught DataIntegrityViolationException ", exception);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.CONFLICT)
                .message("A data conflict occurred. Please try again")
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("Caught IllegalArgumentException ", exception);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Unable to process the request due to invalid input.")
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Caught MethodArgumentNotValidException ", exception);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getObjectName() + errors)
                .build();
        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        log.error("Caught unexpected exception ", exception);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred")
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}
