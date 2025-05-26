package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperTest {
    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
    }

    @Test
    void testTeacherToTeacherDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("gmail")
                .firstName("random")
                .createdAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .updatedAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertNotNull(teacherDto);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
    }

    @Test
    void testTeacherDtoToTeacher() {
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "gmail",
                "random",
                LocalDateTime.of(2025, 1, 17, 14, 15, 59),
                LocalDateTime.of(2025, 1, 17, 14, 15, 59));

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");

        List<TeacherDto> dtoList = List.of(dto1, dto2);
        List<Teacher> entityList = Mappers.getMapper(TeacherMapper.class).toEntity(dtoList);

        assertEquals(2, entityList.size());
        assertEquals("John", entityList.get(0).getFirstName());
        assertEquals("Jane", entityList.get(1).getFirstName());
    }


    @Test
    void testToDtoList() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Tom");
        teacher1.setLastName("Thumb");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Alice");
        teacher2.setLastName("Wonder");

        List<Teacher> teacherList = List.of(teacher1, teacher2);
        List<TeacherDto> dtoList = Mappers.getMapper(TeacherMapper.class).toDto(teacherList);

        assertEquals(2, dtoList.size());
        assertEquals("Tom", dtoList.get(0).getFirstName());
        assertEquals("Alice", dtoList.get(1).getFirstName());
    }



}