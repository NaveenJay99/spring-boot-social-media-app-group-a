package com.group.a.social_media_app.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

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