package com.socialmedia.controller;

import com.socialmedia.dto.UserRegistrationDto;
import com.socialmedia.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "expired", required = false) String expired) {
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        
        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully");
        }
        
        if (expired != null) {
            model.addAttribute("error", "Your session has expired. Please login again");
        }
        
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistration", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRegistration") UserRegistrationDto userRegistrationDto,
                              BindingResult result, 
                              Model model, 
                              RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        // Check if passwords match
        if (!userRegistrationDto.isPasswordsMatching()) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/register";
        }
        
        try {
            // Check if email already exists
            if (userService.existsByEmail(userRegistrationDto.getEmail())) {
                model.addAttribute("error", "An account with this email already exists");
                return "auth/register";
            }
            
            // Register the user
            userService.registerUser(userRegistrationDto);
            
            redirectAttributes.addFlashAttribute("success", 
                "Registration successful! Please login with your credentials.");
            
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
}