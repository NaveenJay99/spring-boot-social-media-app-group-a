package com.group.a.social_media_app.repository;

import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.PostLike;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
    // Find like by user and post (to check if already liked)
    Optional<PostLike> findByUserAndPost(User user, Post post);

    // Count total likes on a post
    long countByPost(Post post);

    // Optional: Get all likes by a specific user
    List<PostLike> findAllByUser(User user);

    // Optional: Delete a like by user and post (for unlike)
    void deleteByUserAndPost(User user, Post post);
}
