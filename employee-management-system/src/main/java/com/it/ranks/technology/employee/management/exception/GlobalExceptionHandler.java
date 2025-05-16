package com.it.ranks.technology.employee.management.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        // Provide a more generic message for DataIntegrityViolationException
        // as ex.getMessage() can expose too much detail about database constraints.
        if (ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getMessage().contains("Unique index or primary key violation")) {
            body.put("message", "A record with the same unique field (e.g., email) already exists.");
        } else {
            body.put("message", "Database error: Email Address Already Exists");
        }
        return new ResponseEntity<>(body, HttpStatus.CONFLICT); // 409 Conflict is often suitable for unique constraint violations
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("message", "An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
