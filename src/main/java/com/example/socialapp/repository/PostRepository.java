package com.example.socialapp.repository;

import com.example.socialapp.entity.Post;
import com.example.socialapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
    
    Page<Post> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.author = :currentUser " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findPostsByCurrentUser(@Param("currentUser") User currentUser, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.author = :currentUser OR p.author IN :friends " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findPostsByCurrentUserAndFriends(@Param("currentUser") User currentUser, 
                                               @Param("friends") Set<User> friends, 
                                               Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.author IN :users ORDER BY p.createdAt DESC")
    List<Post> findPostsByUsersOrderByCreatedAtDesc(@Param("users") Set<User> users);
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.author = :user")
    Long countPostsByUser(@Param("user") User user);
}