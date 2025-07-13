package com.group.a.social_media_app.controller;

import com.group.a.social_media_app.dto.PostDto;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    
    @PostMapping("/posts")
    public String createPost(@Valid @ModelAttribute("newPost") PostDto postDto,
                            BindingResult result,
                            @AuthenticationPrincipal User currentUser,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            // Reload page with errors
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("posts", postService.getUserPosts(currentUser));
            return "home";
        }
        
        try {
            postService.createPost(postDto.getContent(), currentUser);
            redirectAttributes.addFlashAttribute("message", "Post created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create post: " + e.getMessage());
        }
        
        return "redirect:/home";
    }
}