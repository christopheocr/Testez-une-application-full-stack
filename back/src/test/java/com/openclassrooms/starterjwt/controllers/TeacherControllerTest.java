package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnTeacherDto_whenFound() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teacherDto, response.getBody());
    }

    @Test
    void findById_shouldReturnNotFound_whenNull() {
        when(teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void findById_shouldReturnBadRequest_whenInvalidId() {
        ResponseEntity<?> response = teacherController.findById("abc");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void findAll_shouldReturnTeacherDtoList() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);

        when(teacherService.findAll()).thenReturn(List.of(teacher));
        when(teacherMapper.toDto(List.of(teacher))).thenReturn(List.of(teacherDto));

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(teacherDto), response.getBody());
    }
}
