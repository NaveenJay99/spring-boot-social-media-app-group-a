package com.example.socialapp.exception;

public class DuplicateEmailException extends RuntimeException {
    
    public DuplicateEmailException(String message) {
        super(message);
    }
    
    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DuplicateEmailException(String email) {
        super("Email already exists: " + email);
    }
}