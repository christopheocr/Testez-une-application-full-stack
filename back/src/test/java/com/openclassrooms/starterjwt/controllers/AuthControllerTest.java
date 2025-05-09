package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password")
                .build();

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("test-jwt-token");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(
                new User("test@example.com", "Doe", "John", "password", true)
        ));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof JwtResponse);

        JwtResponse jwt = (JwtResponse) response.getBody();
        assertEquals("test-jwt-token", jwt.getToken());
        assertEquals("test@example.com", jwt.getUsername());
        assertEquals("John", jwt.getFirstName());
        assertEquals("Doe", jwt.getLastName());
        assertTrue(jwt.getAdmin());
    }

    @Test
    void testAuthenticateUser_Failure() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("fail@example.com");
        loginRequest.setPassword("badpass");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Authentication failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authController.authenticateUser(loginRequest));
        assertEquals("Authentication failed", exception.getMessage());
    }

    @Test
    void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("new@example.com");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("1234");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encoded-1234");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("taken@example.com");

        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());

        verify(userRepository, never()).save(any());
    }
}
