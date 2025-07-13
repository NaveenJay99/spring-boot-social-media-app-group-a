package com.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Long id;

    @NotBlank(message = "Post content is required")
    @Size(min = 1, max = 1000, message = "Post content must be between 1 and 1000 characters")
    private String content;

    private String authorName;
    private LocalDateTime createdAt;
    private String timeAgo;
}