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
    @Size(min = 1, max = 1000, message = "Post content must be between 1 and 1000 characters")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
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
        LocalDateTime postTime = this.createdAt;

        if (postTime.isAfter(now.minusMinutes(1))) {
            return "Just now";
        } else if (postTime.isAfter(now.minusHours(1))) {
            long minutes = java.time.Duration.between(postTime, now).toMinutes();
            return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        } else if (postTime.isAfter(now.minusDays(1))) {
            long hours = java.time.Duration.between(postTime, now).toHours();
            return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        } else {
            long days = java.time.Duration.between(postTime, now).toDays();
            return days + " day" + (days == 1 ? "" : "s") + " ago";
        }
    }
}