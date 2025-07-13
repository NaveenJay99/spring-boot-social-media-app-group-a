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

    public User registerUser(UserRegistrationDTO registrationDTO) {
        log.info("Registering user with email: {}", registrationDTO.getEmail());

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + registrationDTO.getEmail());
        }

        if (!registrationDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = User.builder()
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deactivateUser(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setIsActive(false);
            userRepository.save(user);
            log.info("Deactivated user: {}", email);
        });
    }
}