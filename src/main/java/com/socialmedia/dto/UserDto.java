package com.socialmedia.dto;

import java.time.LocalDateTime;

public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private int postCount;
    private int friendCount;
    private boolean isFriend;
    private boolean hasPendingRequest;
    private boolean canSendFriendRequest;

    // Constructors
    public UserDto() {
    }

    public UserDto(Long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public UserDto(Long id, String email, String firstName, String lastName, 
                   Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isHasPendingRequest() {
        return hasPendingRequest;
    }

    public void setHasPendingRequest(boolean hasPendingRequest) {
        this.hasPendingRequest = hasPendingRequest;
    }

    public boolean isCanSendFriendRequest() {
        return canSendFriendRequest;
    }

    public void setCanSendFriendRequest(boolean canSendFriendRequest) {
        this.canSendFriendRequest = canSendFriendRequest;
    }

    // Helper methods
    private void updateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }

    public String getInitials() {
        if (firstName != null && lastName != null) {
            return firstName.charAt(0) + "" + lastName.charAt(0);
        }
        return "";
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", postCount=" + postCount +
                ", friendCount=" + friendCount +
                ", isFriend=" + isFriend +
                ", hasPendingRequest=" + hasPendingRequest +
                ", canSendFriendRequest=" + canSendFriendRequest +
                '}';
    }
}