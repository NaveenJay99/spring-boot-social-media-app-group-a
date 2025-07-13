package com.group.a.social_media_app.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column( nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostLike> likes;


    @Column(name = "like_count")
    @Builder.Default
    private Integer likeCount = 0;

    public void incrementLikeCount(){
        this.likeCount++;
    }

    public void decrementLikeCount(){
        this.likeCount--;
    }

    public boolean isLikedBy(User user){
        return this.likes.stream()
                .anyMatch(like -> like.getUser().getId().equals(user.getId()));
    }

}

