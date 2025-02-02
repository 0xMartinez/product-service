package com.example.cassandra.service;

import com.example.cassandra.exception.ResourceNotFoundException;
import com.example.cassandra.exception.ValidationException;
import com.example.cassandra.repository.UserRepository;
import com.example.cassandra.repository.model.User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (password.length() < 5) {
            throw new ValidationException("Password requires minimum length 5");
        }

        String hashedPassword = passwordService.encodePassword(password);
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPassword(hashedPassword);
        user.setUsername(username);
        user.setRoles(Set.of("User"));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username: " + username + " already exist"));
    }
}