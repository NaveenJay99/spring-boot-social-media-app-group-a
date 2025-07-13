package com.socialmedia.controller;

import com.socialmedia.dto.PostDto;
import com.socialmedia.dto.UserDto;
import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import com.socialmedia.service.FriendService;
import com.socialmedia.service.PostService;
import com.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private FriendService friendService;

    @GetMapping("/home")
    public String home(Model model,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size) {
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Create pageable for posts
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        // Get posts for feed (own posts + friends' posts)
        Page<PostDto> feedPosts = postService.convertToDtoPage(postService.findFeedPosts(pageable));
        
        // Get current user info
        UserDto currentUserDto = userService.convertToDto(currentUser);
        
        // Get friend request count
        long pendingRequestCount = friendService.getPendingRequestCount();
        
        // Get friend count
        int friendCount = friendService.getFriendCount(currentUser.getId());
        
        // Get post count
        int postCount = postService.countPostsByUser(currentUser);
        
        // Add attributes to model
        model.addAttribute("currentUser", currentUserDto);
        model.addAttribute("posts", feedPosts);
        model.addAttribute("newPost", new PostDto());
        model.addAttribute("pendingRequestCount", pendingRequestCount);
        model.addAttribute("friendCount", friendCount);
        model.addAttribute("postCount", postCount);
        
        // Pagination attributes
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", feedPosts.getTotalPages());
        model.addAttribute("hasNext", feedPosts.hasNext());
        model.addAttribute("hasPrevious", feedPosts.hasPrevious());
        
        return "home/feed";
    }

    @GetMapping("/users")
    public String users(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(value = "search", required = false) String search) {
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Create pageable for users
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        
        Page<UserDto> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userService.convertToDtoPage(userService.searchUsers(search, pageable));
        } else {
            users = userService.convertToDtoPage(userService.findAllUsersExceptCurrent(pageable));
        }
        
        // Get current user info
        UserDto currentUserDto = userService.convertToDto(currentUser);
        
        // Get friend request count
        long pendingRequestCount = friendService.getPendingRequestCount();
        
        // Add attributes to model
        model.addAttribute("currentUser", currentUserDto);
        model.addAttribute("users", users);
        model.addAttribute("pendingRequestCount", pendingRequestCount);
        model.addAttribute("searchTerm", search);
        
        // Pagination attributes
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("hasNext", users.hasNext());
        model.addAttribute("hasPrevious", users.hasPrevious());
        
        return "home/users";
    }

    @GetMapping("/profile")
    public String profile(Model model,
                         @RequestParam(value = "userId", required = false) Long userId) {
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Determine which user's profile to show
        User profileUser;
        if (userId != null) {
            profileUser = userService.findById(userId);
        } else {
            profileUser = currentUser;
        }

        // Get user's posts
        List<PostDto> userPosts = postService.convertToDtoList(postService.findPostsByAuthor(profileUser));
        
        // Get user DTO
        UserDto userDto = userService.convertToDto(profileUser);
        
        // Get current user info
        UserDto currentUserDto = userService.convertToDto(currentUser);
        
        // Get friend request count
        long pendingRequestCount = friendService.getPendingRequestCount();
        
        // Check if viewing own profile
        boolean isOwnProfile = currentUser.getId().equals(profileUser.getId());
        
        // Add attributes to model
        model.addAttribute("currentUser", currentUserDto);
        model.addAttribute("profileUser", userDto);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("pendingRequestCount", pendingRequestCount);
        model.addAttribute("isOwnProfile", isOwnProfile);
        
        return "home/profile";
    }
}