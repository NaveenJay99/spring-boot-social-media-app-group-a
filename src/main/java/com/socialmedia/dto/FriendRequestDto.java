package com.socialmedia.dto;

import com.socialmedia.entity.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestDto {

    private Long id;
    private Long senderId;
    private String senderName;
    private String senderEmail;
    private Long receiverId;
    private String receiverName;
    private String receiverEmail;
    private FriendRequest.RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Custom constructor for basic friend request creation
    public FriendRequestDto(Long id, Long senderId, String senderName, 
                           Long receiverId, String receiverName, 
                           FriendRequest.RequestStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isPending() {
        return status == FriendRequest.RequestStatus.PENDING;
    }

    public boolean isAccepted() {
        return status == FriendRequest.RequestStatus.ACCEPTED;
    }

    public boolean isDeclined() {
        return status == FriendRequest.RequestStatus.DECLINED;
    }

    public String getStatusDisplayName() {
        switch (status) {
            case PENDING:
                return "Pending";
            case ACCEPTED:
                return "Accepted";
            case DECLINED:
                return "Declined";
            default:
                return "Unknown";
        }
    }

    public String getOtherUserName(Long currentUserId) {
        if (senderId.equals(currentUserId)) {
            return receiverName;
        } else {
            return senderName;
        }
    }

    public Long getOtherUserId(Long currentUserId) {
        if (senderId.equals(currentUserId)) {
            return receiverId;
        } else {
            return senderId;
        }
    }
}