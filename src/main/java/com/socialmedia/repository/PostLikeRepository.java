package com.socialmedia.repository;

import com.socialmedia.entity.Post;
import com.socialmedia.entity.PostLike;
import com.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    /**
     * Find a like by user and post
     */
    Optional<PostLike> findByUserAndPost(User user, Post post);

    /**
     * Check if a user has liked a post
     */
    boolean existsByUserAndPost(User user, Post post);

    /**
     * Find all likes for a post
     */
    List<PostLike> findByPost(Post post);

    /**
     * Find all likes for a post ordered by creation time
     */
    List<PostLike> findByPostOrderByCreatedAtDesc(Post post);

    /**
     * Find all likes by a user
     */
    List<PostLike> findByUser(User user);

    /**
     * Find all likes by a user ordered by creation time
     */
    List<PostLike> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Count likes for a post
     */
    long countByPost(Post post);

    /**
     * Count likes by a user
     */
    long countByUser(User user);

    /**
     * Find posts liked by a user
     */
    @Query("SELECT pl.post FROM PostLike pl WHERE pl.user = :user ORDER BY pl.createdAt DESC")
    List<Post> findLikedPostsByUser(@Param("user") User user);

    /**
     * Find users who liked a post
     */
    @Query("SELECT pl.user FROM PostLike pl WHERE pl.post = :post ORDER BY pl.createdAt DESC")
    List<User> findUsersWhoLikedPost(@Param("post") Post post);

    /**
     * Delete a like by user and post
     */
    void deleteByUserAndPost(User user, Post post);

    /**
     * Delete all likes for a post
     */
    void deleteByPost(Post post);

    /**
     * Delete all likes by a user
     */
    void deleteByUser(User user);

    /**
     * Find most liked posts
     */
    @Query("SELECT pl.post, COUNT(pl) as likeCount FROM PostLike pl " +
           "GROUP BY pl.post ORDER BY likeCount DESC")
    List<Object[]> findMostLikedPosts();

}