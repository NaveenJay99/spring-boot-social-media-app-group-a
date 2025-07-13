package com.example.socialapp.repository;

import com.example.socialapp.entity.FriendRequest;
import com.example.socialapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.FriendRequestStatus status);
    
    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequest.FriendRequestStatus status);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver = :user AND fr.status = 'PENDING' " +
           "ORDER BY fr.createdAt DESC")
    List<FriendRequest> findPendingReceivedRequests(@Param("user") User user);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender = :user AND fr.status = 'PENDING' " +
           "ORDER BY fr.createdAt DESC")
    List<FriendRequest> findPendingSentRequests(@Param("user") User user);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver = :user " +
           "ORDER BY fr.createdAt DESC")
    List<FriendRequest> findAllReceivedRequests(@Param("user") User user);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender = :user " +
           "ORDER BY fr.createdAt DESC")
    List<FriendRequest> findAllSentRequests(@Param("user") User user);
    
    boolean existsBySenderAndReceiver(User sender, User receiver);
    
    @Query("SELECT COUNT(fr) FROM FriendRequest fr WHERE fr.receiver = :user AND fr.status = 'PENDING'")
    Long countPendingRequestsForUser(@Param("user") User user);
}