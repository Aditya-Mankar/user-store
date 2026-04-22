package com.user.userstore;

import com.user.userstore.model.Address;
import com.user.userstore.model.User;
import com.user.userstore.repository.UserRepository;
import com.user.userstore.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        Address address = new Address("Main St", "Mumbai", "India");
        testUser = new User("aditya", "aditya@example.com", address);
    }

    @Test
    void saveUser_shouldReturnSavedUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        User result = userService.saveUser(testUser);

        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void getUserByUsername_shouldReturnUserWhenFound() {
        when(userRepository.findById("aditya")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserByUsername("aditya");

        assertTrue(result.isPresent());
        assertEquals("aditya", result.get().getUsername());
        verify(userRepository, times(1)).findById("aditya");
    }

    @Test
    void getUserByUsername_shouldReturnEmptyWhenNotFound() {
        when(userRepository.findById("unknownUsername")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUsername("unknownUsername");

        assertFalse(result.isPresent());
    }

}
