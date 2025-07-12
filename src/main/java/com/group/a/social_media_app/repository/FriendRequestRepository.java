package com.group.a.social_media_app.repository;

import com.group.a.social_media_app.entity.FriendRequest;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    // Find requests sent by a user
    List<FriendRequest> findBySender(User sender);

    // Find requests received by a user
    List<FriendRequest> findByReceiver(User receiver);

    // Check if a request already exists between two users
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    // Get pending friend requests received by a user
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.Status status);

    // Get accepted friend requests for a user (either sent or received)
    List<FriendRequest> findBySenderOrReceiverAndStatus(User sender, User receiver, FriendRequest.Status status);
}

}
