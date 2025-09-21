package com.civa.platform.shared.interfaces.rest.exception;

import com.civa.platform.shared.application.exceptions.DependencyConflictException;
import com.civa.platform.shared.application.exceptions.ResourceAlreadyException;
import com.civa.platform.shared.application.exceptions.ResourceNotFoundException;
import com.civa.platform.shared.interfaces.rest.resources.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "Resource not found");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyException.class)
    public ResponseEntity<ErrorMessage> handleResourceAlreadyException(ResourceAlreadyException ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT.value(), ex.getMessage(), "Resource already exists");
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DependencyConflictException.class)
    public ResponseEntity<ErrorMessage> handleDependencyConflictException(DependencyConflictException ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT.value(), ex.getMessage(), "Dependency conflict detected");
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Invalid argument");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            "An unexpected error occurred: " + ex.getMessage(), "Internal server error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}