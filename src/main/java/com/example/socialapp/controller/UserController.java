package com.example.socialapp.controller;

import com.example.socialapp.dto.UserDto;
import com.example.socialapp.entity.User;
import com.example.socialapp.service.FriendService;
import com.example.socialapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserService userService;
    private final FriendService friendService;
    
    @GetMapping
    public String showUserDirectory(Model model,
                                   Authentication authentication,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String search) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        log.info("Loading user directory for user: {}", currentUser.getEmail());
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;
        
        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsersExceptCurrent(currentUser.getId(), search.trim(), pageable);
            model.addAttribute("search", search);
        } else {
            users = userService.getAllActiveUsersExceptCurrent(currentUser.getId(), pageable);
        }
        
        // Convert users to DTOs with relationship status
        List<UserDto> userDtos = users.getContent().stream()
                .map(user -> {
                    UserDto dto = userService.convertToDto(user, currentUser);
                    dto.setHasPendingFriendRequest(friendService.hasPendingFriendRequest(currentUser.getId(), user.getId()));
                    dto.setHasReceivedFriendRequest(friendService.hasPendingFriendRequest(user.getId(), currentUser.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
        
        // Get friend request count for notifications
        Long pendingRequestCount = friendService.getPendingRequestCount(currentUser.getId());
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userDtos);
        model.addAttribute("pendingRequestCount", pendingRequestCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("hasNext", users.hasNext());
        model.addAttribute("hasPrevious", users.hasPrevious());
        
        return "users";
    }
    
    @GetMapping("/{userId}")
    public String showUserProfile(@PathVariable Long userId,
                                 Model model,
                                 Authentication authentication) {
        
        User currentUser = userService.findByEmail(authentication.getName());
        User user = userService.findById(userId);
        
        log.info("Loading profile for user: {} by user: {}", user.getEmail(), currentUser.getEmail());
        
        UserDto userDto = userService.convertToDto(user, currentUser);
        userDto.setHasPendingFriendRequest(friendService.hasPendingFriendRequest(currentUser.getId(), user.getId()));
        userDto.setHasReceivedFriendRequest(friendService.hasPendingFriendRequest(user.getId(), currentUser.getId()));
        
        // Get friend request count for notifications
        Long pendingRequestCount = friendService.getPendingRequestCount(currentUser.getId());
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", userDto);
        model.addAttribute("pendingRequestCount", pendingRequestCount);
        
        return "profile";
    }
}