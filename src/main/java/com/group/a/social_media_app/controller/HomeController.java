package com.group.a.social_media_app.controller;

import com.group.a.social_media_app.dto.PostDTO;
import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.service.CustomUserDetailsService;
import com.group.a.social_media_app.service.PostService;
import com.group.a.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserService userService;

    /**
     * Create a new post
     */
    @PostMapping("/create")
    public String createPost(
            @Valid @ModelAttribute("newPost") PostDTO postDTO,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Creating post for user: {}", principal.getUsername());

        // Validate post content
        if (result.hasErrors()) {
            log.warn("Post validation errors for user: {}", principal.getUsername());
            redirectAttributes.addFlashAttribute("error", "Post content is required and must be between 1 and 1000 characters.");
            return "redirect:/home";
        }

        try {
            // Get current user
            Optional<User> userOptional = userService.findByEmail(principal.getUsername());
            if (userOptional.isEmpty()) {
                log.error("User not found: {}", principal.getUsername());
                redirectAttributes.addFlashAttribute("error", "User not found. Please log in again.");
                return "redirect:/login";
            }

            User currentUser = userOptional.get();

            // Create post
            Post createdPost = postService.createPost(postDTO, currentUser);

            log.info("Post created successfully with ID: {} for user: {}",
                    createdPost.getId(), principal.getUsername());

            redirectAttributes.addFlashAttribute("success", "Post created successfully!");
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Error creating post for user: {}", principal.getUsername(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to create post. Please try again.");
            return "redirect:/home";
        }
    }

    /**
     * Delete a post
     */
    @PostMapping("/delete/{id}")
    public String deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
            RedirectAttributes redirectAttributes) {

        log.info("Deleting post with ID: {} for user: {}", id, principal.getUsername());

        try {
            // Verify post exists and belongs to current user
            Optional<Post> postOptional = postService.findById(id);
            if (postOptional.isEmpty()) {
                log.warn("Post not found with ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Post not found.");
                return "redirect:/home";
            }

            Post post = postOptional.get();
            if (!post.getUser().getEmail().equals(principal.getUsername())) {
                log.warn("User {} attempted to delete post {} that doesn't belong to them",
                        principal.getUsername(), id);
                redirectAttributes.addFlashAttribute("error", "You can only delete your own posts.");
                return "redirect:/home";
            }

            postService.deletePost(id);
            log.info("Post deleted successfully with ID: {} for user: {}", id, principal.getUsername());

            redirectAttributes.addFlashAttribute("success", "Post deleted successfully!");
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Error deleting post with ID: {} for user: {}", id, principal.getUsername(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete post. Please try again.");
            return "redirect:/home";
        }
    }

    /**
     * Edit post form (optional enhancement)
     */
    @GetMapping("/edit/{id}")
    public String editPostForm(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Loading edit form for post ID: {} for user: {}", id, principal.getUsername());

        try {
            Optional<Post> postOptional = postService.findById(id);
            if (postOptional.isEmpty()) {
                log.warn("Post not found with ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Post not found.");
                return "redirect:/home";
            }

            Post post = postOptional.get();
            if (!post.getUser().getEmail().equals(principal.getUsername())) {
                log.warn("User {} attempted to edit post {} that doesn't belong to them",
                        principal.getUsername(), id);
                redirectAttributes.addFlashAttribute("error", "You can only edit your own posts.");
                return "redirect:/home";
            }

            PostDTO postDTO = postService.convertToDTO(post);
            model.addAttribute("post", postDTO);
            model.addAttribute("currentUser", post.getUser());

            return "home/edit-post";
        } catch (Exception e) {
            log.error("Error loading edit form for post ID: {} for user: {}", id, principal.getUsername(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to load post for editing. Please try again.");
            return "redirect:/home";
        }
    }

    /**
     * Update post
     */
    @PostMapping("/update/{id}")
    public String updatePost(
            @PathVariable Long id,
            @Valid @ModelAttribute("post") PostDTO postDTO,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Updating post with ID: {} for user: {}", id, principal.getUsername());

        if (result.hasErrors()) {
            log.warn("Post validation errors for post ID: {} for user: {}", id, principal.getUsername());
            model.addAttribute("error", "Post content is required and must be between 1 and 1000 characters.");
            return "home/edit-post";
        }

        try {
            Optional<Post> postOptional = postService.findById(id);
            if (postOptional.isEmpty()) {
                log.warn("Post not found with ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Post not found.");
                return "redirect:/home";
            }

            Post post = postOptional.get();
            if (!post.getUser().getEmail().equals(principal.getUsername())) {
                log.warn("User {} attempted to update post {} that doesn't belong to them",
                        principal.getUsername(), id);
                redirectAttributes.addFlashAttribute("error", "You can only edit your own posts.");
                return "redirect:/home";
            }

            post.setContent(postDTO.getContent());
            postService.updatePost(post);

            log.info("Post updated successfully with ID: {} for user: {}", id, principal.getUsername());
            redirectAttributes.addFlashAttribute("success", "Post updated successfully!");
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Error updating post with ID: {} for user: {}", id, principal.getUsername(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to update post. Please try again.");
            return "redirect:/home";
        }
    }
}
