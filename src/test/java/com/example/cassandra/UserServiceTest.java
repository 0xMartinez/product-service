package com.example.cassandra;

import com.example.cassandra.exception.ResourceNotFoundException;
import com.example.cassandra.exception.ValidationException;
import com.example.cassandra.repository.UserRepository;
import com.example.cassandra.repository.model.User;
import com.example.cassandra.service.PasswordService;
import com.example.cassandra.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testUser");
        testUser.setPassword("hashedPassword");
        testUser.setRoles(Set.of("User"));
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Given
        String rawPassword = "password123";
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordService.encodePassword(rawPassword)).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User registeredUser = userService.registerUser("testUser", rawPassword);

        // Then
        assertNotNull(registeredUser);
        assertEquals("testUser", registeredUser.getUsername());
        assertEquals("hashedPassword", registeredUser.getPassword());
        assertTrue(registeredUser.getRoles().contains("User"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser("testUser", "password123"));

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordTooShort() {
        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () ->
                userService.registerUser("testUser", "1234"));

        assertEquals("Password requires minimum length 5", exception.getMessage());
    }

    @Test
    void shouldFindUserByUsername() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        // When
        User foundUser = userService.findByUsername("testUser");

        // Then
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.findByUsername("unknownUser"));

        assertEquals("Username: unknownUser already exist", exception.getMessage());
    }
}
