package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    private User createTestUser() {
        return new User("test@example.com", "Doe", "John", "password", false);
    }

    @Test
    void findById_shouldReturnUserDto_whenUserExists() {
        User user = createTestUser();
        user.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void findById_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsNotANumber() {
        ResponseEntity<?> response = userController.findById("abc");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void delete_shouldReturnOk_whenUserMatchesPrincipal() {
        User user = createTestUser();
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);

        // Simule un utilisateur authentifié avec le bon email
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        AuthenticationTokenInContext(userDetails);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    void delete_shouldReturnUnauthorized_whenUserMismatch() {
        User user = createTestUser();
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("other@example.com");
        AuthenticationTokenInContext(userDetails);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(401, response.getStatusCodeValue());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void delete_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = userController.save("invalid");

        assertEquals(400, response.getStatusCodeValue());
    }


    private void AuthenticationTokenInContext(UserDetails userDetails) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new TestingAuthenticationToken(userDetails, null));
        SecurityContextHolder.setContext(context);
    }
}
