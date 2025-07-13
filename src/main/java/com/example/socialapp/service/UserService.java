package com.example.socialapp.service;

import com.example.socialapp.dto.UserDto;
import com.example.socialapp.dto.UserRegistrationDto;
import com.example.socialapp.entity.User;
import com.example.socialapp.exception.DuplicateEmailException;
import com.example.socialapp.exception.UserNotFoundException;
import com.example.socialapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getIsActive())
                .build();
    }
    
    public User registerUser(UserRegistrationDto registrationDto) {
        log.info("Registering user with email: {}", registrationDto.getEmail());
        
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + registrationDto.getEmail());
        }
        
        User user = User.builder()
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .isActive(true)
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        return savedUser;
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    
    public Page<User> getAllActiveUsersExceptCurrent(Long currentUserId, Pageable pageable) {
        return userRepository.findAllActiveUsersExceptCurrent(currentUserId, pageable);
    }
    
    public Page<User> searchUsersExceptCurrent(Long currentUserId, String search, Pageable pageable) {
        return userRepository.findActiveUsersBySearchExceptCurrent(currentUserId, search, pageable);
    }
    
    public Set<User> getFriends(Long userId) {
        return userRepository.findFriendsByUserId(userId);
    }
    
    public UserDto convertToDto(User user, User currentUser) {
        boolean isFriend = currentUser.isFriendWith(user);
        
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .isFriend(isFriend)
                .build();
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public void deleteUser(Long userId) {
        User user = findById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }
    
    public long getTotalUsers() {
        return userRepository.count();
    }
}