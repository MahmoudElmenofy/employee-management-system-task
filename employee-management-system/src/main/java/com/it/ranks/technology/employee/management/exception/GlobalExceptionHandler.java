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
//exception layer handles exception across the whole application in a clean and centralized way
@ControllerAdvice         //makes a class a global error handler
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    //Catches: ResourceNotFoundException (custom exception) like "message": "Employee not found" and Returns HTTP 404
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /*Catches: validation failures on request body (like missing or invalid fields) and Returns HTTP 400
        for example:  "email": "Email is required",
                      "name": "Name must be at least 2 characters"    */
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
    /*Catches: database errors, usually constraint violations,
               Returns HTTP 409 if a unique constraint is violated,
               Checks for specific cause message like "Unique index or primary key violation" */
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        //ex.getMessage() can expose too much detail about database constraints.
        if (ex.getCause() != null && ex.getCause().getCause() != null &&
                ex.getCause().getCause().getMessage().contains("Unique index or primary key violation")) {
            body.put("message", "A record with the same unique field (e.g., email) already exists.");
        } else {
            body.put("message", "Database error: Email Address Already Exists");
        }
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    /*Catches: all other exceptions, Returns HTTP 500
        Response body includes a message from the exception:
        "message": "An unexpected error occurred: NullPointerException" */
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        Map<String, String> body = new HashMap<>();
        body.put("message", "An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
