package com.group.a.social_media_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})

public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who liked the post
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The post that was liked
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
