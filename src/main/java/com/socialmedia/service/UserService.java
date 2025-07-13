package com.socialmedia.service;

import com.socialmedia.dto.UserDto;
import com.socialmedia.dto.UserRegistrationDto;
import com.socialmedia.entity.User;
import com.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FriendService friendService;

    @Autowired
    private PostService postService;

    public User registerUser(UserRegistrationDto registrationDto) {
        // Check if user already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("User with email " + registrationDto.getEmail() + " already exists");
        }

        // Check if passwords match
        if (!registrationDto.isPasswordsMatching()) {
            throw new RuntimeException("Passwords do not match");
        }

        // Create new user
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setIsActive(true);

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return findByEmail(email);
        }
        return null;
    }

    public Long getCurrentUserId() {
        User currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAllUsersExceptCurrent(Pageable pageable) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Page.empty();
        }
        return userRepository.findAllExceptUser(currentUser.getId(), pageable);
    }

    public Page<User> findPotentialFriends(Pageable pageable) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Page.empty();
        }
        return userRepository.findPotentialFriends(currentUser.getId(), pageable);
    }

    public Page<User> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.findByNameContaining(searchTerm, pageable);
    }

    public List<User> findFriends(Long userId) {
        return userRepository.findFriends(userId);
    }

    public UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        
        // Set additional fields
        dto.setPostCount(postService.countPostsByUser(user));
        dto.setFriendCount(friendService.getFriendCount(user.getId()));
        
        User currentUser = getCurrentUser();
        if (currentUser != null && !currentUser.getId().equals(user.getId())) {
            dto.setFriend(friendService.areUsersFriends(currentUser.getId(), user.getId()));
            dto.setHasPendingRequest(friendService.hasPendingRequest(currentUser.getId(), user.getId()));
            dto.setCanSendFriendRequest(!dto.isFriend() && !dto.isHasPendingRequest());
        }
        
        return dto;
    }

    public List<UserDto> convertToDtoList(List<User> users) {
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Page<UserDto> convertToDtoPage(Page<User> users) {
        return users.map(this::convertToDto);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    public long getActiveUserCount() {
        return userRepository.countByIsActiveTrue();
    }

    public void deactivateUser(Long userId) {
        User user = findById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long userId) {
        User user = findById(userId);
        user.setIsActive(true);
        userRepository.save(user);
    }
}