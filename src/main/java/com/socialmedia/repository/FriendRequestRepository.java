package com.socialmedia.repository;

import com.socialmedia.entity.FriendRequest;
import com.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    /**
     * Find friend request between two users
     */
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    /**
     * Check if friend request exists between two users (in either direction)
     */
    @Query("SELECT fr FROM FriendRequest fr WHERE " +
           "(fr.sender = :user1 AND fr.receiver = :user2) OR " +
           "(fr.sender = :user2 AND fr.receiver = :user1)")
    Optional<FriendRequest> findBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * Find all pending friend requests sent by a user
     */
    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequest.RequestStatus status);

    /**
     * Find all pending friend requests received by a user
     */
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.RequestStatus status);

    /**
     * Find all pending friend requests sent by a user
     */
    List<FriendRequest> findBySenderAndStatusOrderByCreatedAtDesc(User sender, FriendRequest.RequestStatus status);

    /**
     * Find all pending friend requests received by a user
     */
    List<FriendRequest> findByReceiverAndStatusOrderByCreatedAtDesc(User receiver, FriendRequest.RequestStatus status);

    /**
     * Find all accepted friend requests for a user (in either direction)
     */
    @Query("SELECT fr FROM FriendRequest fr WHERE " +
           "(fr.sender = :user OR fr.receiver = :user) " +
           "AND fr.status = 'ACCEPTED'")
    List<FriendRequest> findAcceptedFriendRequests(@Param("user") User user);

    /**
     * Check if two users are friends
     */
    @Query("SELECT COUNT(fr) > 0 FROM FriendRequest fr WHERE " +
           "((fr.sender = :user1 AND fr.receiver = :user2) OR " +
           "(fr.sender = :user2 AND fr.receiver = :user1)) " +
           "AND fr.status = 'ACCEPTED'")
    boolean areUsersFriends(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * Check if there's a pending request between two users
     */
    @Query("SELECT COUNT(fr) > 0 FROM FriendRequest fr WHERE " +
           "((fr.sender = :user1 AND fr.receiver = :user2) OR " +
           "(fr.sender = :user2 AND fr.receiver = :user1)) " +
           "AND fr.status = 'PENDING'")
    boolean hasPendingRequest(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * Count pending requests for a user
     */
    long countByReceiverAndStatus(User receiver, FriendRequest.RequestStatus status);

    /**
     * Count sent requests for a user
     */
    long countBySenderAndStatus(User sender, FriendRequest.RequestStatus status);

    /**
     * Find all requests involving a user
     */
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender = :user OR fr.receiver = :user")
    List<FriendRequest> findAllInvolvingUser(@Param("user") User user);

    /**
     * Delete all requests between two users
     */
    void deleteBySenderAndReceiver(User sender, User receiver);

}