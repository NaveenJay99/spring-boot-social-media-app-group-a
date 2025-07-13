package com.socialmedia.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomExceptions.UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(CustomExceptions.UserNotFoundException ex, HttpServletRequest request) {
        logger.error("User not found: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("error", "User not found");
        mv.addObject("message", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(CustomExceptions.PostNotFoundException.class)
    public ModelAndView handlePostNotFoundException(CustomExceptions.PostNotFoundException ex, HttpServletRequest request) {
        logger.error("Post not found: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("error", "Post not found");
        mv.addObject("message", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(CustomExceptions.DuplicateEmailException.class)
    public ModelAndView handleDuplicateEmailException(CustomExceptions.DuplicateEmailException ex, HttpServletRequest request) {
        logger.error("Duplicate email: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("auth/register");
        mv.addObject("error", "Email already exists");
        mv.addObject("message", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(CustomExceptions.UnauthorizedException ex, HttpServletRequest request) {
        logger.error("Unauthorized access: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("error/403");
        mv.addObject("error", "Unauthorized access");
        mv.addObject("message", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(CustomExceptions.FriendRequestException.class)
    public ModelAndView handleFriendRequestException(CustomExceptions.FriendRequestException ex, HttpServletRequest request) {
        logger.error("Friend request error: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("home/users");
        mv.addObject("error", "Friend request error");
        mv.addObject("message", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.error("Validation error: {}", ex.getMessage());
        
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        
        bindingResult.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        
        ModelAndView mv = new ModelAndView("auth/register");
        mv.addObject("errors", errors);
        mv.addObject("error", "Validation failed");
        return mv;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        logger.error("Authentication error: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("auth/login");
        mv.addObject("error", "Authentication failed");
        mv.addObject("message", "Invalid username or password");
        return mv;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ModelAndView handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        logger.error("Bad credentials: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("auth/login");
        mv.addObject("error", "Invalid credentials");
        mv.addObject("message", "Please check your email and password");
        return mv;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        logger.error("Access denied: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("error/403");
        mv.addObject("error", "Access denied");
        mv.addObject("message", "You don't have permission to access this resource");
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("error", "Internal server error");
        mv.addObject("message", "An unexpected error occurred. Please try again later.");
        return mv;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("Runtime error: {}", ex.getMessage(), ex);
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("error", "Runtime error");
        mv.addObject("message", ex.getMessage());
        return mv;
    }

    // For AJAX requests
    @ExceptionHandler(CustomExceptions.PostLikeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handlePostLikeException(CustomExceptions.PostLikeException ex) {
        logger.error("Post like error: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", "Like operation failed");
        response.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}