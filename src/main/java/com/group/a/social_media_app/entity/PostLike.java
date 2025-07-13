package com.group.a.social_media_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who liked the post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Post that was liked
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
