package com.example.socialapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        log.error("User not found: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        return modelAndView;
    }
    
    @ExceptionHandler(DuplicateEmailException.class)
    public ModelAndView handleDuplicateEmailException(DuplicateEmailException ex, HttpServletRequest request) {
        log.error("Duplicate email error: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.addObject("userRegistrationDto", new com.example.socialapp.dto.UserRegistrationDto());
        return modelAndView;
    }
    
    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<Map<String, String>> handleFriendRequestException(FriendRequestException ex) {
        log.error("Friend request error: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ModelAndView modelAndView = new ModelAndView();
        String requestURI = request.getRequestURI();
        
        if (requestURI.contains("register")) {
            modelAndView.setViewName("register");
            modelAndView.addObject("userRegistrationDto", new com.example.socialapp.dto.UserRegistrationDto());
        } else if (requestURI.contains("login")) {
            modelAndView.setViewName("login");
            modelAndView.addObject("userLoginDto", new com.example.socialapp.dto.UserLoginDto());
        } else {
            modelAndView.setViewName("home");
        }
        
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ModelAndView handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Authentication error: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("error", "Invalid email or password");
        modelAndView.addObject("userLoginDto", new com.example.socialapp.dto.UserLoginDto());
        return modelAndView;
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", "Access denied");
        modelAndView.addObject("status", HttpStatus.FORBIDDEN.value());
        return modelAndView;
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", "An unexpected error occurred");
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return modelAndView;
    }
}