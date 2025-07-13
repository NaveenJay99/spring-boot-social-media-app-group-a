package com.group.a.social_media_app.repository;

import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByEmail(String email);
    
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts WHERE u.email = :email")
    Optional<User> findByEmailWithPosts(@Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    Optional<User> findActiveUsers();
}
