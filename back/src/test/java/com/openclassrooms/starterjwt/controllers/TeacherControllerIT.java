package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        teacherRepository.deleteAll();

        User user = new User("user@example.com", "Doe", "John", passwordEncoder.encode("password"), true);
        userRepository.save(user);

        LoginRequest loginPayload = new LoginRequest();
        loginPayload.setEmail("user@example.com");
        loginPayload.setPassword("password");

        var result = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload))
        ).andExpect(status().isOk()).andReturn();

        token = "Bearer " + objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void testFindTeacherById_shouldReturnOk() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        mockMvc.perform(get("/api/teacher/" + teacher.getId())
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testFindTeacherById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/99999")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindTeacherById_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/teacher/invalid-id")
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindAllTeachers_shouldReturnOk() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Alice");
        teacher1.setLastName("Smith");

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Bob");
        teacher2.setLastName("Johnson");

        teacherRepository.saveAll(List.of(teacher1, teacher2));

        mockMvc.perform(get("/api/teacher")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Alice")))
                .andExpect(jsonPath("$[1].firstName", is("Bob")));
    }
}
