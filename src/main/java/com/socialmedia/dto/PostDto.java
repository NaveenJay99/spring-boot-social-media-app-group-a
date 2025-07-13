package com.socialmedia.dto;

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
    @Size(min = 1, max = 2000, message = "Post content must be between 1 and 2000 characters")
    private String content;

    private String authorName;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount;
    private boolean likedByCurrentUser;

    // Custom constructor for basic post creation
    public PostDto(String content) {
        this.content = content;
    }

    // Helper methods
    public String getShortContent() {
        if (content == null) return "";
        return content.length() > 100 ? content.substring(0, 100) + "..." : content;
    }

    public boolean isNew() {
        return id == null;
    }
}