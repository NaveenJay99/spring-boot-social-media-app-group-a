package com.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

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

    // Constructors
    public PostDto() {
    }

    public PostDto(String content) {
        this.content = content;
    }

    public PostDto(Long id, String content, String authorName, Long authorId, 
                   LocalDateTime createdAt, int likeCount, boolean likedByCurrentUser) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    // Helper methods
    public String getShortContent() {
        if (content == null) return "";
        return content.length() > 100 ? content.substring(0, 100) + "..." : content;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorId=" + authorId +
                ", createdAt=" + createdAt +
                ", likeCount=" + likeCount +
                ", likedByCurrentUser=" + likedByCurrentUser +
                '}';
    }
}