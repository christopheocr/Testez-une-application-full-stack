package com.openclassrooms.starterjwt.mapper;


import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    private Teacher teacher;
    private User user;
    private Session session;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("gmail");
        teacher.setFirstName("random");

        user = new User();
        user.setId(1L);
        user.setEmail("random@gmail.com");
        user.setLastName("gmail");
        user.setFirstName("random");
        user.setPassword("password");
        user.setAdmin(true);

        session = new Session();
        session.setId(1L);
        session.setName("Relax With Yoga");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session.");
        session.setTeacher(teacher);
        session.setUsers(Collections.singletonList(user));
        session.setCreatedAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59));
        session.setUpdatedAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59));
    }

    @Test
    void testSessionToSessionDto() {
        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
        assertEquals(session.getUsers().get(0).getId(), sessionDto.getUsers().get(0));
        assertEquals(session.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDto.getUpdatedAt());
    }
    @Test
    void testToEntity_withValidDtoList() {
        SessionDto dto1 = new SessionDto(1L, "Relax With Yoga", new Date(), 1L, "A relaxing yoga session.", Collections.singletonList(1L), LocalDateTime.now(), LocalDateTime.now());
        List<SessionDto> dtoList = Arrays.asList(dto1);

        List<Session> entityList = sessionMapper.toEntity(dtoList);

        assertNotNull(entityList);
        assertEquals(1, entityList.size());

        assertEquals(dto1.getId(), entityList.get(0).getId());
        assertEquals(dto1.getName(), entityList.get(0).getName());
    }

    @Test
    void testSessionDtoToSession() {
        SessionDto sessionDto = new SessionDto(
                1L,
                "Relax With Yoga",
                new Date(),
                1L,
                "A relaxing yoga session.",
                Collections.singletonList(1L),
                LocalDateTime.of(2025, 1, 17, 14, 15, 59),
                LocalDateTime.of(2025, 1, 17, 14, 15, 59));

        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(userService.findById(anyLong())).thenReturn(user);

        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDate(), session.getDate());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
        assertEquals(sessionDto.getUsers().get(0), session.getUsers().get(0).getId());
        assertEquals(sessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), session.getUpdatedAt());
    }

    @Test
    void testToDtoList() {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");
        session1.setDescription("Description 1");
        session1.setDate(new Date());
        Teacher teacher = new Teacher(); teacher.setId(10L);
        session1.setTeacher(teacher);
        session1.setUsers(List.of());

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");
        session2.setDescription("Description 2");
        session2.setDate(new Date());
        session2.setTeacher(null);
        session2.setUsers(null);

        List<SessionDto> dtos = sessionMapper.toDto(List.of(session1, session2));

        assertEquals(2, dtos.size());
        assertEquals("Session 1", dtos.get(0).getName());
        assertEquals("Session 2", dtos.get(1).getName());
        assertEquals(10L, dtos.get(0).getTeacher_id());
        assertNull(dtos.get(1).getTeacher_id());
    }

    @Test
    void testToDto_withNullTeacher() {
        Session session = new Session();
        session.setId(301L);
        session.setName("No teacher");
        session.setDescription("No teacher session");
        session.setDate(new Date());
        session.setTeacher(null);
        session.setUsers(List.of());

        SessionDto dto = sessionMapper.toDto(session);

        assertNotNull(dto);
        assertNull(dto.getTeacher_id());
    }



}