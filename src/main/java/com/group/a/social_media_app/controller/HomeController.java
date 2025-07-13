package com.group.a.social_media_app.controller;

import com.group.a.social_media_app.dto.PostDto;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final PostService postService;
    
    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal User currentUser, Model model) {
        List<PostDto> userPosts = postService.getUserPosts(currentUser);
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", userPosts);
        model.addAttribute("newPost", new PostDto());
        
        return "home";
    }
}