package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;

    private User user;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());
        user = new User();
        user.setId(2L);
    }

    @Test
    void create_shouldSaveAndReturnSession() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertEquals(session, result);
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_shouldCallDeleteById() {
        sessionService.delete(1L);
        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll_shouldReturnListOfSessions() {
        List<Session> sessions = List.of(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(1, result.size());
        verify(sessionRepository).findAll();
    }

    @Test
    void getById_shouldReturnSessionIfFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertEquals(session, result);
        verify(sessionRepository).findById(1L);
    }

    @Test
    void getById_shouldReturnNullIfNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
    }

    @Test
    void update_shouldSetIdAndSaveSession() {
        Session updatedSession = new Session();
        updatedSession.setUsers(new ArrayList<>());

        when(sessionRepository.save(any(Session.class))).thenReturn(updatedSession);

        Session result = sessionService.update(1L, updatedSession);

        assertEquals(updatedSession, result);
        assertEquals(1L, updatedSession.getId());
        verify(sessionRepository).save(updatedSession);
    }

    @Test
    void participate_shouldAddUserToSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        sessionService.participate(1L, 2L);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_shouldThrowNotFound_whenSessionOrUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void participate_shouldThrowBadRequest_whenUserAlreadyParticipates() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void noLongerParticipate_shouldRemoveUser() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 2L);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void noLongerParticipate_shouldThrowNotFound_whenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 2L));
    }

    @Test
    void noLongerParticipate_shouldThrowBadRequest_whenUserNotParticipant() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 2L));
    }
}
