package com.example.socialapp.repository;

import com.example.socialapp.entity.Like;
import com.example.socialapp.entity.Post;
import com.example.socialapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByUserAndPost(User user, Post post);
    
    List<Like> findByPost(Post post);
    
    List<Like> findByUser(User user);
    
    boolean existsByUserAndPost(User user, Post post);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post")
    Long countLikesByPost(@Param("post") Post post);
    
    @Query("SELECT l.user FROM Like l WHERE l.post = :post")
    List<User> findUsersByPost(@Param("post") Post post);
    
    void deleteByUserAndPost(User user, Post post);
}