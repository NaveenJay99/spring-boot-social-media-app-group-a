package com.group.a.social_media_app.controller;

import com.group.a.social_media_app.dto.UserRegistrationDTO;
import com.group.a.social_media_app.exception.UserAlreadyExistsException;
import com.group.a.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    /**
     * Root redirect to login
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    /**
     * Display login form
     */
    @GetMapping("/login")
    public String loginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model,
            Principal principal) {

        // If user is already authenticated, redirect to home
        if (principal != null) {
            return "redirect:/home";
        }

        if (error != null) {
            model.addAttribute("error", "Invalid email or password. Please try again.");
        }

        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully.");
        }

        if (expired != null) {
            model.addAttribute("error", "Your session has expired. Please log in again.");
        }

        return "auth/login";
    }

    /**
     * Display registration form
     */
    @GetMapping("/register")
    public String registrationForm(Model model, Principal principal) {
        // If user is already authenticated, redirect to home
        if (principal != null) {
            return "redirect:/home";
        }

        model.addAttribute("user", new UserRegistrationDTO());
        return "auth/register";
    }

    /**
     * Process user registration
     */
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") UserRegistrationDTO registrationDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Processing registration for email: {}", registrationDTO.getEmail());

        // Check for validation errors
        if (result.hasErrors()) {
            log.warn("Registration validation errors for email: {}", registrationDTO.getEmail());
            return "auth/register";
        }

        // Check if passwords match
        if (!registrationDTO.isPasswordMatching()) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "auth/register";
        }

        try {
            userService.registerUser(registrationDTO);
            log.info("User registered successfully: {}", registrationDTO.getEmail());
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please log in with your credentials.");
            return "redirect:/login";
        } catch (UserAlreadyExistsException e) {
            log.warn("Registration failed - user already exists: {}", registrationDTO.getEmail());
            result.rejectValue("email", "error.user", "User with this email already exists");
            return "auth/register";
        } catch (Exception e) {
            log.error("Registration failed for email: {}", registrationDTO.getEmail(), e);
            model.addAttribute("error", "Registration failed. Please try again.");
            return "auth/register";
        }
    }

    /**
     * Handle logout (handled by Spring Security)
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
}