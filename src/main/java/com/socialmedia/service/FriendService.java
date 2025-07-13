package com.socialmedia.service;

import com.socialmedia.dto.FriendRequestDto;
import com.socialmedia.entity.FriendRequest;
import com.socialmedia.entity.User;
import com.socialmedia.repository.FriendRequestRepository;
import com.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        User sender = userService.findById(senderId);
        User receiver = userService.findById(receiverId);
        
        // Check if request already exists
        if (friendRequestRepository.findBetweenUsers(sender, receiver).isPresent()) {
            throw new RuntimeException("Friend request already exists between these users");
        }
        
        // Check if users are the same
        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Cannot send friend request to yourself");
        }
        
        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        return friendRequestRepository.save(friendRequest);
    }

    public FriendRequest acceptFriendRequest(Long requestId) {
        FriendRequest friendRequest = findById(requestId);
        User currentUser = userService.getCurrentUser();
        
        // Check if current user is the receiver
        if (!friendRequest.getReceiver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only accept friend requests sent to you");
        }
        
        // Check if request is pending
        if (!friendRequest.isPending()) {
            throw new RuntimeException("Friend request is not pending");
        }
        
        friendRequest.accept();
        return friendRequestRepository.save(friendRequest);
    }

    public FriendRequest declineFriendRequest(Long requestId) {
        FriendRequest friendRequest = findById(requestId);
        User currentUser = userService.getCurrentUser();
        
        // Check if current user is the receiver
        if (!friendRequest.getReceiver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only decline friend requests sent to you");
        }
        
        // Check if request is pending
        if (!friendRequest.isPending()) {
            throw new RuntimeException("Friend request is not pending");
        }
        
        friendRequest.decline();
        return friendRequestRepository.save(friendRequest);
    }

    public void cancelFriendRequest(Long requestId) {
        FriendRequest friendRequest = findById(requestId);
        User currentUser = userService.getCurrentUser();
        
        // Check if current user is the sender
        if (!friendRequest.getSender().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only cancel friend requests you sent");
        }
        
        // Check if request is pending
        if (!friendRequest.isPending()) {
            throw new RuntimeException("Friend request is not pending");
        }
        
        friendRequestRepository.delete(friendRequest);
    }

    public void removeFriend(Long friendId) {
        User currentUser = userService.getCurrentUser();
        User friend = userService.findById(friendId);
        
        // Find the accepted friend request
        FriendRequest friendRequest = friendRequestRepository.findBetweenUsers(currentUser, friend)
                .orElseThrow(() -> new RuntimeException("No friend request found between these users"));
        
        if (!friendRequest.isAccepted()) {
            throw new RuntimeException("Users are not friends");
        }
        
        friendRequestRepository.delete(friendRequest);
    }

    public List<FriendRequestDto> getPendingReceivedRequests() {
        User currentUser = userService.getCurrentUser();
        List<FriendRequest> requests = friendRequestRepository
                .findByReceiverAndStatusOrderByCreatedAtDesc(currentUser, FriendRequest.RequestStatus.PENDING);
        return convertToDtoList(requests);
    }

    public List<FriendRequestDto> getPendingSentRequests() {
        User currentUser = userService.getCurrentUser();
        List<FriendRequest> requests = friendRequestRepository
                .findBySenderAndStatusOrderByCreatedAtDesc(currentUser, FriendRequest.RequestStatus.PENDING);
        return convertToDtoList(requests);
    }

    public List<User> getFriends(Long userId) {
        return userRepository.findFriends(userId);
    }

    public List<User> getCurrentUserFriends() {
        User currentUser = userService.getCurrentUser();
        return currentUser != null ? getFriends(currentUser.getId()) : List.of();
    }

    public boolean areUsersFriends(Long userId1, Long userId2) {
        User user1 = userService.findById(userId1);
        User user2 = userService.findById(userId2);
        return friendRequestRepository.areUsersFriends(user1, user2);
    }

    public boolean hasPendingRequest(Long userId1, Long userId2) {
        User user1 = userService.findById(userId1);
        User user2 = userService.findById(userId2);
        return friendRequestRepository.hasPendingRequest(user1, user2);
    }

    public int getFriendCount(Long userId) {
        return getFriends(userId).size();
    }

    public long getPendingRequestCount() {
        User currentUser = userService.getCurrentUser();
        return currentUser != null ? 
                friendRequestRepository.countByReceiverAndStatus(currentUser, FriendRequest.RequestStatus.PENDING) : 0;
    }

    private FriendRequest findById(Long id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friend request not found with id: " + id));
    }

    public FriendRequestDto convertToDto(FriendRequest friendRequest) {
        FriendRequestDto dto = new FriendRequestDto();
        dto.setId(friendRequest.getId());
        dto.setSenderId(friendRequest.getSender().getId());
        dto.setSenderName(friendRequest.getSenderFullName());
        dto.setSenderEmail(friendRequest.getSender().getEmail());
        dto.setReceiverId(friendRequest.getReceiver().getId());
        dto.setReceiverName(friendRequest.getReceiverFullName());
        dto.setReceiverEmail(friendRequest.getReceiver().getEmail());
        dto.setStatus(friendRequest.getStatus());
        dto.setCreatedAt(friendRequest.getCreatedAt());
        dto.setUpdatedAt(friendRequest.getUpdatedAt());
        return dto;
    }

    public List<FriendRequestDto> convertToDtoList(List<FriendRequest> friendRequests) {
        return friendRequests.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}