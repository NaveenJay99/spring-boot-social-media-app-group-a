package com.group.a.social_media_app.controller;



import com.group.a.social_media_app.dto.UserRegistrationDTO;
import com.group.a.social_media_app.exception.UserAlreadyExistsException;
import com.group.a.social_media_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model,
                        Principal principal) {
        if (principal != null) return "redirect:/home";
        if (error != null) model.addAttribute("error", "Invalid email or password");
        if (logout != null) model.addAttribute("success", "You have been logged out");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model, Principal principal) {
        if (principal != null) return "redirect:/home";
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegistrationDTO dto,
                           BindingResult result,
                           RedirectAttributes redirect,
                           Model model) {
        if (result.hasErrors()) return "register";
        if (!dto.isPasswordMatching()) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "register";
        }

        try {
            userService.registerUser(dto);
            redirect.addFlashAttribute("success", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (UserAlreadyExistsException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Try again.");
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
}
