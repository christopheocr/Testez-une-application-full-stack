package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnSessionDto_whenFound() {
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void findById_shouldReturnNotFound_whenSessionIsNull() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdInvalid() {
        ResponseEntity<?> response = sessionController.findById("abc");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void findAll_shouldReturnSessionList() {
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();

        when(sessionService.findAll()).thenReturn(List.of(session));
        when(sessionMapper.toDto(List.of(session))).thenReturn(List.of(sessionDto));

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(sessionDto), response.getBody());
    }

    @Test
    void create_shouldReturnCreatedSession() {
        SessionDto dto = new SessionDto();
        dto.setName("Test");
        Session session = new Session();
        session.setName("Test");

        when(sessionMapper.toEntity(dto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        ResponseEntity<?> response = sessionController.create(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void update_shouldReturnUpdatedSession() {
        SessionDto dto = new SessionDto();
        dto.setName("Updated");
        Session session = new Session();
        session.setName("Updated");

        when(sessionMapper.toEntity(dto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        ResponseEntity<?> response = sessionController.update("1", dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void update_shouldReturnBadRequest_whenIdInvalid() {
        SessionDto dto = new SessionDto();
        ResponseEntity<?> response = sessionController.update("invalid", dto);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void delete_shouldReturnOk_whenSessionExists() {
        Session session = new Session();
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).delete(1L);
    }

    @Test
    void delete_shouldReturnNotFound_whenSessionNull() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void delete_shouldReturnBadRequest_whenIdInvalid() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void participate_shouldReturnOk() {
        ResponseEntity<?> response = sessionController.participate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void participate_shouldReturnBadRequest_whenIdsInvalid() {
        ResponseEntity<?> response = sessionController.participate("one", "two");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void noLongerParticipate_shouldReturnOk() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    void noLongerParticipate_shouldReturnBadRequest_whenIdsInvalid() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("x", "y");

        assertEquals(400, response.getStatusCodeValue());
    }
}
