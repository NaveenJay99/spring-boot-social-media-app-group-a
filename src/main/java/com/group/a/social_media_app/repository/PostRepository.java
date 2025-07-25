package com.group.a.social_media_app.repository;

import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user); // Get all posts authored by a specific user

    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    long countByUser(User user);

    List<Post> findByUserOrderByCreatedAtDesc(User user);
}
