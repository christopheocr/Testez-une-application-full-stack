package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        Long userId = 1L;
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findById_shouldReturnNull_whenUserDoesNotExist() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = userService.findById(userId);

        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }
}
