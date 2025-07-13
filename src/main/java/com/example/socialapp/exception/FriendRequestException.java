package com.example.socialapp.exception;

public class FriendRequestException extends RuntimeException {
    
    public FriendRequestException(String message) {
        super(message);
    }
    
    public FriendRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static FriendRequestException duplicateRequest() {
        return new FriendRequestException("Friend request already sent");
    }
    
    public static FriendRequestException selfRequest() {
        return new FriendRequestException("Cannot send friend request to yourself");
    }
    
    public static FriendRequestException alreadyFriends() {
        return new FriendRequestException("Users are already friends");
    }
    
    public static FriendRequestException requestNotFound() {
        return new FriendRequestException("Friend request not found");
    }
    
    public static FriendRequestException invalidAction() {
        return new FriendRequestException("Invalid action on friend request");
    }
}