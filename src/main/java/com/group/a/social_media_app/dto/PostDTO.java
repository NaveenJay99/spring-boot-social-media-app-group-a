package com.group.a.social_media_app.dto;

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
public class PostDTO {

    private Long id;

    @NotBlank(message = "Post content is required")
    @Size(min = 1, max = 1000, message = "Post must be between 1 and 1000 characters")
    private String content;

    private String authorName;

    private LocalDateTime createdAt;

    private String timeAgo;

}
