package com.group.a.social_media_app.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, Model model) {
        log.warn("User already exists: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "auth/register";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleValidationErrors(ConstraintViolationException ex, Model model) {
        log.warn("Validation error: {}", ex.getMessage());
        model.addAttribute("error", "Validation error: " + ex.getMessage());
        return "error/validation-error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected error: ", ex);
        model.addAttribute("error", "Something went wrong. Please try again.");
        return "error/generic-error";
    }
}
