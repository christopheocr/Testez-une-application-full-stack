package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private Long userId;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        User user = new User("user@example.com", "Doe", "John", passwordEncoder.encode("password"), false);
        user = userRepository.save(user);
        userId = user.getId();

        LoginRequest loginPayload = new LoginRequest();
        loginPayload.setEmail("user@example.com");
        loginPayload.setPassword("password");

        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andReturn();

        token = "Bearer " + objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void testFindUserById_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("user@example.com")));
    }

    @Test
    void testFindUserById_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/invalid")
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindUserById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/user/99999")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/user/" + userId)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/invalid")
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUser_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/user/99999")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_shouldReturnUnauthorized_whenEmailMismatch() throws Exception {
        User other = new User("other@example.com", "X", "Y", passwordEncoder.encode("pwd"), false);
        other = userRepository.save(other);

        mockMvc.perform(delete("/api/user/" + other.getId())
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }
}
