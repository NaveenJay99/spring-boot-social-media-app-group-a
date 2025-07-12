package com.group.a.social_media_app.repository;


import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PostRepository extends JpaRepository<Post, Long>{
    // Find all posts by a specific user
    List<Post> findByUser(User user);

    // Optional: Find all posts by user ID
    List<Post> findByUserId(Long userId);

    // Optional: Order posts by latest first
    List<Post> findAllByOrderByCreatedAtDesc();

}
