package com.example.socialapp.controller;

import com.example.socialapp.dto.PostDto;
import com.example.socialapp.entity.Post;
import com.example.socialapp.entity.User;
import com.example.socialapp.service.FriendService;
import com.example.socialapp.service.PostService;
import com.example.socialapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    
    private final PostService postService;
    private final UserService userService;
    private final FriendService friendService;
    
    @GetMapping("/home")
    public String home(Model model, 
                      Authentication authentication,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Loading home page for user: {}", currentUser.getEmail());
        
        // Create pageable for posts
        Pageable pageable = PageRequest.of(page, size);
        
        // Get home feed (user's posts + friends' posts)
        Page<Post> posts = postService.getHomeFeed(currentUser, pageable);
        
        // Convert posts to DTOs
        List<PostDto> postDtos = posts.getContent().stream()
                .map(post -> postService.convertToDto(post, currentUser))
                .collect(Collectors.toList());
        
        // Get friend request count for notifications
        Long pendingRequestCount = friendService.getPendingRequestCount(currentUser.getId());
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", postDtos);
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("pendingRequestCount", pendingRequestCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("hasNext", posts.hasNext());
        model.addAttribute("hasPrevious", posts.hasPrevious());
        
        return "home";
    }
    
    @PostMapping("/home/create-post")
    public String createPost(@Valid @ModelAttribute PostDto postDto,
                           BindingResult bindingResult,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Creating post by user: {}", currentUser.getEmail());
        
        if (bindingResult.hasErrors()) {
            log.warn("Post creation validation errors: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("error", "Post content is required and must be under 500 characters");
            return "redirect:/home";
        }
        
        try {
            postService.createPost(postDto.getContent(), currentUser);
            log.info("Post created successfully by user: {}", currentUser.getEmail());
            redirectAttributes.addFlashAttribute("message", "Post created successfully!");
            
        } catch (Exception e) {
            log.error("Error creating post for user: {}", currentUser.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Error creating post. Please try again.");
        }
        
        return "redirect:/home";
    }
    
    @PostMapping("/home/delete-post/{postId}")
    public String deletePost(@PathVariable Long postId,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Deleting post: {} by user: {}", postId, currentUser.getEmail());
        
        try {
            postService.deletePost(postId, currentUser);
            redirectAttributes.addFlashAttribute("message", "Post deleted successfully!");
            
        } catch (Exception e) {
            log.error("Error deleting post: {} by user: {}", postId, currentUser.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Error deleting post. " + e.getMessage());
        }
        
        return "redirect:/home";
    }
}