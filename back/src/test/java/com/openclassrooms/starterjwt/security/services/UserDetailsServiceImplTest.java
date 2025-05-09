package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);

        User user = new User("user@example.com", "Doe", "John", "pwd", true);
        user.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("user@example.com");

        assertNotNull(result);
        assertEquals("user@example.com", result.getUsername());
        assertTrue(result instanceof UserDetailsImpl);
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("notfound@example.com"));
    }
}
