package com.example.socialapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    
    private Long id;
    
    @NotBlank(message = "Post content is required")
    @Size(min = 1, max = 500, message = "Post content must be between 1 and 500 characters")
    private String content;
    
    private String authorName;
    private Long authorId;
    private LocalDateTime createdAt;
    private Integer likeCount;
    private boolean likedByCurrentUser;
}