package com.socialmedia.dto;

import com.socialmedia.entity.FriendRequest;

import java.time.LocalDateTime;

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

    // Constructors
    public FriendRequestDto() {
    }

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public FriendRequest.RequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequest.RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    @Override
    public String toString() {
        return "FriendRequestDto{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", senderName='" + senderName + '\'' +
                ", receiverId=" + receiverId +
                ", receiverName='" + receiverName + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}