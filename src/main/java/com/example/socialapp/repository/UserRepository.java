package com.example.socialapp.repository;

import com.example.socialapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.id != :currentUserId")
    Page<User> findAllActiveUsersExceptCurrent(@Param("currentUserId") Long currentUserId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.id != :currentUserId AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findActiveUsersBySearchExceptCurrent(@Param("currentUserId") Long currentUserId, 
                                                   @Param("search") String search, 
                                                   Pageable pageable);
    
    @Query("SELECT u.friends FROM User u WHERE u.id = :userId")
    Set<User> findFriendsByUserId(@Param("userId") Long userId);
}