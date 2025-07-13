package com.group.a.social_media_app.repository;


import com.group.a.social_media_app.entity.FriendRequest;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // Find pending requests sent to a user
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.FriendRequestStatus status);

    // Find pending requests sent by a user
    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequest.FriendRequestStatus status);

    // Check if a request exists between two users (to avoid duplicates)
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    // Find all accepted friend requests involving a user
    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender = :user OR fr.receiver = :user) AND fr.status = 'ACCEPTED'")
    List<FriendRequest> findAcceptedFriendRequests(@Param("user") User user);
}
