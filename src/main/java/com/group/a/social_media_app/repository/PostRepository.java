package com.group.a.social_media_app.repository;

import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Find all posts by a user (author)
    List<Post> findByAuthor(User author);

    // Find all posts from a user's friends (for news feed)
    @Query("SELECT p FROM Post p WHERE p.author IN :friends ORDER BY p.createdAt DESC")
    List<Post> findPostsByFriends(@Param("friends") List<User> friends);

    // Find top-liked posts (for trending)
    @Query("SELECT p FROM Post p ORDER BY p.likeCount DESC")
    List<Post> findTopLikedPosts();
}

