package com.example.socialapp.controller;

import com.example.socialapp.dto.FriendRequestDto;
import com.example.socialapp.entity.User;
import com.example.socialapp.service.FriendService;
import com.example.socialapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendController {
    
    private final FriendService friendService;
    private final UserService userService;
    
    @GetMapping
    public String showFriendRequests(Model model, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Loading friend requests for user: {}", currentUser.getEmail());
        
        List<FriendRequestDto> receivedRequests = friendService.getPendingReceivedRequests(currentUser.getId());
        List<FriendRequestDto> sentRequests = friendService.getPendingSentRequests(currentUser.getId());
        
        // Get friend request count for notifications
        Long pendingRequestCount = friendService.getPendingRequestCount(currentUser.getId());
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("receivedRequests", receivedRequests);
        model.addAttribute("sentRequests", sentRequests);
        model.addAttribute("pendingRequestCount", pendingRequestCount);
        
        return "friends";
    }
    
    @PostMapping("/send-request")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendFriendRequest(@RequestParam Long receiverId,
                                                                Authentication authentication) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Sending friend request from user: {} to user: {}", currentUser.getId(), receiverId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            friendService.sendFriendRequest(currentUser.getId(), receiverId);
            response.put("success", true);
            response.put("message", "Friend request sent successfully!");
            
            log.info("Friend request sent successfully from user: {} to user: {}", currentUser.getId(), receiverId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error sending friend request from user: {} to user: {}", currentUser.getId(), receiverId, e);
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/accept-request")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> acceptFriendRequest(@RequestParam Long requestId,
                                                                  Authentication authentication) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Accepting friend request: {} by user: {}", requestId, currentUser.getId());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            friendService.acceptFriendRequest(requestId, currentUser.getId());
            response.put("success", true);
            response.put("message", "Friend request accepted successfully!");
            
            log.info("Friend request accepted successfully: {} by user: {}", requestId, currentUser.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error accepting friend request: {} by user: {}", requestId, currentUser.getId(), e);
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/decline-request")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> declineFriendRequest(@RequestParam Long requestId,
                                                                   Authentication authentication) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Declining friend request: {} by user: {}", requestId, currentUser.getId());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            friendService.declineFriendRequest(requestId, currentUser.getId());
            response.put("success", true);
            response.put("message", "Friend request declined successfully!");
            
            log.info("Friend request declined successfully: {} by user: {}", requestId, currentUser.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error declining friend request: {} by user: {}", requestId, currentUser.getId(), e);
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/remove-friend")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFriend(@RequestParam Long friendId,
                                                           Authentication authentication) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Removing friend: {} by user: {}", friendId, currentUser.getId());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            friendService.removeFriend(currentUser.getId(), friendId);
            response.put("success", true);
            response.put("message", "Friend removed successfully!");
            
            log.info("Friend removed successfully: {} by user: {}", friendId, currentUser.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error removing friend: {} by user: {}", friendId, currentUser.getId(), e);
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Traditional form-based actions for non-AJAX requests
    @PostMapping("/send-request-form")
    public String sendFriendRequestForm(@RequestParam Long receiverId,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        
        try {
            friendService.sendFriendRequest(currentUser.getId(), receiverId);
            redirectAttributes.addFlashAttribute("message", "Friend request sent successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/users";
    }
    
    @PostMapping("/accept-request-form")
    public String acceptFriendRequestForm(@RequestParam Long requestId,
                                         Authentication authentication,
                                         RedirectAttributes redirectAttributes) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        
        try {
            friendService.acceptFriendRequest(requestId, currentUser.getId());
            redirectAttributes.addFlashAttribute("message", "Friend request accepted successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/friends";
    }
    
    @PostMapping("/decline-request-form")
    public String declineFriendRequestForm(@RequestParam Long requestId,
                                          Authentication authentication,
                                          RedirectAttributes redirectAttributes) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        
        try {
            friendService.declineFriendRequest(requestId, currentUser.getId());
            redirectAttributes.addFlashAttribute("message", "Friend request declined successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/friends";
    }
}