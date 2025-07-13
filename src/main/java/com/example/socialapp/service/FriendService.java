package com.example.socialapp.service;

import com.example.socialapp.dto.FriendRequestDto;
import com.example.socialapp.entity.FriendRequest;
import com.example.socialapp.entity.User;
import com.example.socialapp.exception.FriendRequestException;
import com.example.socialapp.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendService {
    
    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;
    
    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        log.info("Sending friend request from user: {} to user: {}", senderId, receiverId);
        
        if (senderId.equals(receiverId)) {
            throw FriendRequestException.selfRequest();
        }
        
        User sender = userService.findById(senderId);
        User receiver = userService.findById(receiverId);
        
        if (sender.isFriendWith(receiver)) {
            throw FriendRequestException.alreadyFriends();
        }
        
        if (friendRequestRepository.existsBySenderAndReceiver(sender, receiver)) {
            throw FriendRequestException.duplicateRequest();
        }
        
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequest.FriendRequestStatus.PENDING)
                .build();
        
        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        log.info("Friend request sent successfully with id: {}", savedRequest.getId());
        return savedRequest;
    }
    
    public FriendRequest acceptFriendRequest(Long requestId, Long userId) {
        log.info("Accepting friend request: {} by user: {}", requestId, userId);
        
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(FriendRequestException::requestNotFound);
        
        if (!friendRequest.getReceiver().getId().equals(userId)) {
            throw FriendRequestException.invalidAction();
        }
        
        if (!friendRequest.isPending()) {
            throw FriendRequestException.invalidAction();
        }
        
        friendRequest.accept();
        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        
        // Add users as friends
        User sender = friendRequest.getSender();
        User receiver = friendRequest.getReceiver();
        sender.addFriend(receiver);
        
        userService.updateUser(sender);
        userService.updateUser(receiver);
        
        log.info("Friend request accepted successfully. Users {} and {} are now friends", 
                sender.getEmail(), receiver.getEmail());
        return savedRequest;
    }
    
    public FriendRequest declineFriendRequest(Long requestId, Long userId) {
        log.info("Declining friend request: {} by user: {}", requestId, userId);
        
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(FriendRequestException::requestNotFound);
        
        if (!friendRequest.getReceiver().getId().equals(userId)) {
            throw FriendRequestException.invalidAction();
        }
        
        if (!friendRequest.isPending()) {
            throw FriendRequestException.invalidAction();
        }
        
        friendRequest.decline();
        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        
        log.info("Friend request declined successfully");
        return savedRequest;
    }
    
    public List<FriendRequestDto> getPendingReceivedRequests(Long userId) {
        User user = userService.findById(userId);
        List<FriendRequest> requests = friendRequestRepository.findPendingReceivedRequests(user);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<FriendRequestDto> getPendingSentRequests(Long userId) {
        User user = userService.findById(userId);
        List<FriendRequest> requests = friendRequestRepository.findPendingSentRequests(user);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<FriendRequestDto> getAllReceivedRequests(Long userId) {
        User user = userService.findById(userId);
        List<FriendRequest> requests = friendRequestRepository.findAllReceivedRequests(user);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<FriendRequestDto> getAllSentRequests(Long userId) {
        User user = userService.findById(userId);
        List<FriendRequest> requests = friendRequestRepository.findAllSentRequests(user);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public void removeFriend(Long userId, Long friendId) {
        log.info("Removing friend relationship between users: {} and {}", userId, friendId);
        
        User user = userService.findById(userId);
        User friend = userService.findById(friendId);
        
        if (!user.isFriendWith(friend)) {
            throw new RuntimeException("Users are not friends");
        }
        
        user.removeFriend(friend);
        userService.updateUser(user);
        userService.updateUser(friend);
        
        log.info("Friend relationship removed successfully");
    }
    
    public boolean areFriends(Long userId1, Long userId2) {
        User user1 = userService.findById(userId1);
        User user2 = userService.findById(userId2);
        return user1.isFriendWith(user2);
    }
    
    public boolean hasPendingFriendRequest(Long senderId, Long receiverId) {
        User sender = userService.findById(senderId);
        User receiver = userService.findById(receiverId);
        
        Optional<FriendRequest> request = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        return request.isPresent() && request.get().isPending();
    }
    
    public Long getPendingRequestCount(Long userId) {
        User user = userService.findById(userId);
        return friendRequestRepository.countPendingRequestsForUser(user);
    }
    
    private FriendRequestDto convertToDto(FriendRequest friendRequest) {
        return FriendRequestDto.builder()
                .id(friendRequest.getId())
                .senderId(friendRequest.getSender().getId())
                .senderName(friendRequest.getSender().getFullName())
                .senderEmail(friendRequest.getSender().getEmail())
                .receiverId(friendRequest.getReceiver().getId())
                .receiverName(friendRequest.getReceiver().getFullName())
                .receiverEmail(friendRequest.getReceiver().getEmail())
                .status(friendRequest.getStatus())
                .createdAt(friendRequest.getCreatedAt())
                .build();
    }
}