package com.example.socialapp.controller;

import com.example.socialapp.dto.UserLoginDto;
import com.example.socialapp.dto.UserRegistrationDto;
import com.example.socialapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String showLoginForm(Model model,
                               @RequestParam(name = "error", required = false) String error,
                               @RequestParam(name = "logout", required = false) String logout,
                               @RequestParam(name = "expired", required = false) String expired) {
        
        model.addAttribute("userLoginDto", new UserLoginDto());
        
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        
        if (expired != null) {
            model.addAttribute("error", "Your session has expired. Please login again.");
        }
        
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserRegistrationDto userRegistrationDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        log.info("Processing registration for email: {}", userRegistrationDto.getEmail());
        
        if (bindingResult.hasErrors()) {
            log.warn("Registration validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("userRegistrationDto", userRegistrationDto);
            return "register";
        }
        
        try {
            userService.registerUser(userRegistrationDto);
            log.info("User registered successfully: {}", userRegistrationDto.getEmail());
            
            redirectAttributes.addFlashAttribute("message", 
                "Registration successful! Please login with your credentials.");
            return "redirect:/login";
            
        } catch (Exception e) {
            log.error("Registration failed for email: {}", userRegistrationDto.getEmail(), e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userRegistrationDto", userRegistrationDto);
            return "register";
        }
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
}