package com.group.a.social_media_app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            Model model,
            HttpServletRequest request) {

        log.warn("User already exists exception: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());

        // Return appropriate view based on request URI
        if (request.getRequestURI().contains("/register")) {
            return "auth/register";
        }

        return "auth/login";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(
            AccessDeniedException ex,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.warn("Access denied: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", "Access denied. Please log in to continue.");
        return "redirect:/login";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(
            IllegalArgumentException ex,
            Model model,
            HttpServletRequest request) {

        log.warn("Illegal argument exception: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());

        // Return appropriate view based on request URI
        if (request.getRequestURI().contains("/register")) {
            return "auth/register";
        } else if (request.getRequestURI().contains("/home")) {
            return "home/feed";
        }

        return "auth/login";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(
            Exception ex,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        String errorMessage = "An unexpected error occurred. Please try again.";

        // Handle based on request URI
        if (request.getRequestURI().contains("/register")) {
            model.addAttribute("error", errorMessage);
            return "auth/register";
        } else if (request.getRequestURI().contains("/login")) {
            model.addAttribute("error", errorMessage);
            return "auth/login";
        } else if (request.getRequestURI().contains("/home") ||
                request.getRequestURI().contains("/posts")) {
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/home";
        }

        // Default fallback
        redirect
