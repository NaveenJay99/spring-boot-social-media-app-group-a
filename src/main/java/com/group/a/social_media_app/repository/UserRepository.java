package com.group.a.social_media_app.repository;

import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login)
    Optional<User> findByEmail(String email);

    // Check if a user exists by email (for registration)
    boolean existsByEmail(String email);

    // Find all friends of a user
    @Query("SELECT u.friends FROM User u WHERE u.id = :userId")
    List<User> findFriendsByUserId(@Param("userId") Long userId);

    // Search users by name (first or last name)
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(concat('%', :query, '%')) OR LOWER(u.lastName) LIKE LOWER(concat('%', :query, '%'))")
    List<User> searchByName(@Param("query") String query);

}
