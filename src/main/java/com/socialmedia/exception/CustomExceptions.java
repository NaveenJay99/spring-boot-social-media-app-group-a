package com.socialmedia.exception;

public class CustomExceptions {

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class PostNotFoundException extends RuntimeException {
        public PostNotFoundException(String message) {
            super(message);
        }
    }

    public static class FriendRequestNotFoundException extends RuntimeException {
        public FriendRequestNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class FriendRequestException extends RuntimeException {
        public FriendRequestException(String message) {
            super(message);
        }
    }

    public static class PostLikeException extends RuntimeException {
        public PostLikeException(String message) {
            super(message);
        }
    }
}