package com.group.a.social_media_app.repository;

import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.PostLike;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // Check if a user has already liked a post
    Optional<PostLike> findByUserAndPost(User user, Post post);

    // Count likes for a post
    long countByPost(Post post);

    // Delete like by user and post (unlike)
    void deleteByUserAndPost(User user, Post post);
}
