package com.example.socialapp.controller;

import com.example.socialapp.dto.LikeDto;
import com.example.socialapp.entity.Post;
import com.example.socialapp.entity.User;
import com.example.socialapp.service.LikeService;
import com.example.socialapp.service.PostService;
import com.example.socialapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    
    private final PostService postService;
    private final LikeService likeService;
    private final UserService userService;
    
    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postId,
                                                         Authentication authentication) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Toggling like for post: {} by user: {}", postId, currentUser.getEmail());
        
        try {
            Post post = postService.findById(postId);
            LikeDto likeDto = likeService.toggleLike(post, currentUser);
            
            // Update post like count
            postService.updateLikeCount(post);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("liked", likeDto.isLiked());
            response.put("likeCount", likeDto.getLikeCount());
            response.put("postId", postId);
            
            log.info("Like toggled successfully for post: {} by user: {}", postId, currentUser.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error toggling like for post: {} by user: {}", postId, currentUser.getEmail(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Error processing like. Please try again.");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/{postId}/like-status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@PathVariable Long postId,
                                                            Authentication authentication) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        
        try {
            Post post = postService.findById(postId);
            boolean isLiked = likeService.isPostLikedByUser(post, currentUser);
            Long likeCount = likeService.getLikeCountForPost(post);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("liked", isLiked);
            response.put("likeCount", likeCount);
            response.put("postId", postId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error getting like status for post: {} by user: {}", postId, currentUser.getEmail(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Error getting like status");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}