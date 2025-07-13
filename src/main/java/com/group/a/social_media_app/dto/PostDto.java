package com.group.a.social_media_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    
    private Long id;
    
    @NotBlank(message = "Post content is required")
    @Size(max = 1000, message = "Post content cannot exceed 1000 characters")
    private String content;
    
    private String authorName;
    private LocalDateTime createdAt;
}