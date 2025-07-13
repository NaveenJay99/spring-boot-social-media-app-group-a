package com.group.a.social_media_app.service;

import com.group.a.social_media_app.dto.UserRegistrationDTO;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.exception.UserAlreadyExistsException;
import com.group.a.social_media_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * @param registrationDTO the registration data
     * @return the created user
     * @throws UserAlreadyExistsException if user already exists
     */
    public User registerUser(UserRegistrationDTO registrationDTO) {
        log.info("Attempting to register user with email: {}", registrationDTO.getEmail());

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            log.warn("User registration failed: email already exists - {}", registrationDTO.getEmail());
            throw new UserAlreadyExistsException("User with email " + registrationDTO.getEmail() + " already exists");
        }

        if (!registrationDTO.isPasswordMatching()) {
            log.warn("User registration failed: password mismatch for email - {}", registrationDTO.getEmail());
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = User.builder()
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with email: {}", savedUser.getEmail());

        return savedUser;
    }

    /**
     * Find user by email
     * @param email the email address
     * @return Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find user by email with posts
     * @param email the email address
     * @return Optional containing the user with posts if found
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmailWithPosts(String email) {
        return userRepository.findByEmailWithPosts(email);
    }

    /**
     * Check if user exists by email
     * @param email the email address
     * @return true if user exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Find user by ID
     * @param id the user ID
     * @return Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Update user information
     * @param user the user to update
     * @return the updated user
     */
    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        return userRepository.save(user);
    }

    /**
     * Deactivate user account
     * @param email the email address
     */
    public void deactivateUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(false);
            userRepository.save(user);
            log.info("User deactivated: {}", email);
        }
    }

    /**
     * Validate user registration data
     * @param registrationDTO the registration data
     * @throws IllegalArgumentException if validation fails
     */
    public void validateRegistration(UserRegistrationDTO registrationDTO) {
        if (!registrationDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (existsByEmail(registrationDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registrationDTO.getEmail() + " already exists");
        }
    }
}