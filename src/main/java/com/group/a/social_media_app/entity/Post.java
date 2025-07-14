package com.group.a.social_media_app.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    @NotBlank(message = "Post content is required")
    @Size(max = 1000, message = "Post content must be between 1 and 1000 characters")
    private String content;

    @Column(name = "created_at",  updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getTimeAgo() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) return "";
        long minutes = java.time.Duration.between(createdAt, now).toMinutes();
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minute(s) ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + " hour(s) ago";
        long days = hours / 24;
        return days + " day(s) ago";
    }

}