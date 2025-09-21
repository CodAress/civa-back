package com.civa.platform.shared.application.exceptions;

public class DependencyConflictException extends RuntimeException {
    public DependencyConflictException(String message) {
        super(message);
    }
}