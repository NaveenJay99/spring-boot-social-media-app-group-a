package com.socialmedia.repository;

import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all posts by author
     */
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);

    /**
     * Find all posts by author with pagination
     */
    Page<Post> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);

    /**
     * Find all posts by author ID
     */
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    /**
     * Find posts by multiple authors (for friend feed)
     */
    @Query("SELECT p FROM Post p WHERE p.author.id IN :authorIds ORDER BY p.createdAt DESC")
    List<Post> findByAuthorIds(@Param("authorIds") List<Long> authorIds);

    /**
     * Find posts by multiple authors with pagination
     */
    @Query("SELECT p FROM Post p WHERE p.author.id IN :authorIds ORDER BY p.createdAt DESC")
    Page<Post> findByAuthorIds(@Param("authorIds") List<Long> authorIds, Pageable pageable);

    /**
     * Find posts for user's feed (own posts + friends' posts)
     */
    @Query("SELECT p FROM Post p WHERE p.author.id = :userId OR p.author.id IN (" +
           "SELECT CASE WHEN fr.sender.id = :userId THEN fr.receiver.id " +
           "ELSE fr.sender.id END " +
           "FROM FriendRequest fr " +
           "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId) " +
           "AND fr.status = 'ACCEPTED') " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findFeedPosts(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find posts containing specific text
     */
    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findByContentContaining(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Count posts by author
     */
    long countByAuthor(User author);

    /**
     * Count posts by author ID
     */
    long countByAuthorId(Long authorId);

    /**
     * Find recent posts (last 24 hours)
     */
    @Query("SELECT p FROM Post p WHERE p.createdAt >= CURRENT_TIMESTAMP - INTERVAL '1 day' " +
           "ORDER BY p.createdAt DESC")
    List<Post> findRecentPosts();

    /**
     * Find most liked posts
     */
    @Query("SELECT p FROM Post p LEFT JOIN p.likes l GROUP BY p ORDER BY COUNT(l) DESC")
    Page<Post> findMostLikedPosts(Pageable pageable);

}