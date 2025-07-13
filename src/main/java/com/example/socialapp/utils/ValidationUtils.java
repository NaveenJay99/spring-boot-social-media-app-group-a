package com.example.socialapp.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtils {
    
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    private static final String NAME_REGEX = "^[A-Za-z\\s]{2,50}$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.length() <= 50;
    }
    
    public static boolean isValidPostContent(String content) {
        return content != null && !content.trim().isEmpty() && content.length() <= 500;
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.trim();
    }
    
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}