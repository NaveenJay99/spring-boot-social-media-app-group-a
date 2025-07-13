package com.socialmedia.repository;

import com.socialmedia.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Find all active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Find all users except the specified user (for user directory)
     */
    @Query("SELECT u FROM User u WHERE u.id != :userId AND u.isActive = true")
    Page<User> findAllExceptUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find users by first name or last name containing the search term
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find friends of a user (users who have accepted friend requests)
     */
    @Query("SELECT DISTINCT u FROM User u WHERE u.id IN (" +
           "SELECT CASE WHEN fr.sender.id = :userId THEN fr.receiver.id " +
           "ELSE fr.sender.id END " +
           "FROM FriendRequest fr " +
           "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId) " +
           "AND fr.status = 'ACCEPTED')")
    List<User> findFriends(@Param("userId") Long userId);

    /**
     * Find potential friends (users who are not friends and have no pending requests)
     */
    @Query("SELECT u FROM User u WHERE u.id != :userId AND u.isActive = true AND " +
           "u.id NOT IN (SELECT CASE WHEN fr.sender.id = :userId THEN fr.receiver.id " +
           "ELSE fr.sender.id END FROM FriendRequest fr " +
           "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId))")
    Page<User> findPotentialFriends(@Param("userId") Long userId, Pageable pageable);

    /**
     * Count active users
     */
    long countByIsActiveTrue();

}