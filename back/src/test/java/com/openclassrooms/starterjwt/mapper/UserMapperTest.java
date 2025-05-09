package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testUserToUserDto() {
        User user = User.builder()
                .id(1L)
                .email("random@gmail.com")
                .lastName("gmail")
                .firstName("random")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .updatedAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .build();

        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }

    @Test
    void testUserDtoToUser() {
        UserDto userDto = new UserDto(
                1L,
                "random@gmail.com",
                "gmail",
                "random",
                true,
                "password",
                LocalDateTime.of(2025, 1, 17, 14, 15, 59),
                LocalDateTime.of(2025, 1, 17, 14, 15, 59));

        User user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        UserDto userDto1 = new UserDto(
                1L,
                "random@gmail.com",
                "gmail",
                "random",
                true,
                "password",
                LocalDateTime.of(2025, 1, 17, 14, 15, 59),
                LocalDateTime.of(2025, 1, 17, 14, 15, 59));
        UserDto userDto2 = new UserDto(
                2L,
                "Margot@example.com",
                "DELAHAYE",
                "Margot",
                false,
                "password123",
                LocalDateTime.of(2025, 1, 17, 14, 15, 59),
                LocalDateTime.of(2025, 1, 17, 14, 15, 59));

        List<User> users = userMapper.toEntity(Arrays.asList(userDto1, userDto2));

        assertNotNull(users);
        assertEquals(2, users.size());

        User user1 = users.get(0);
        User user2 = users.get(1);

        assertEquals(userDto1.getId(), user1.getId());
        assertEquals(userDto1.getEmail(), user1.getEmail());
        assertEquals(userDto1.getLastName(), user1.getLastName());
        assertEquals(userDto1.getFirstName(), user1.getFirstName());
        assertEquals(userDto1.isAdmin(), user1.isAdmin());
        assertEquals(userDto1.getCreatedAt(), user1.getCreatedAt());
        assertEquals(userDto1.getUpdatedAt(), user1.getUpdatedAt());

        assertEquals(userDto2.getId(), user2.getId());
        assertEquals(userDto2.getEmail(), user2.getEmail());
        assertEquals(userDto2.getLastName(), user2.getLastName());
        assertEquals(userDto2.getFirstName(), user2.getFirstName());
        assertEquals(userDto2.isAdmin(), user2.isAdmin());
        assertEquals(userDto2.getCreatedAt(), user2.getCreatedAt());
        assertEquals(userDto2.getUpdatedAt(), user2.getUpdatedAt());
    }

    @Test
    void testToDtoList() {
        User user1 = User.builder()
                .id(1L)
                .email("random@gmail.com")
                .lastName("gmail")
                .firstName("random")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .updatedAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("Margot@example.com")
                .lastName("DELAHAYE")
                .firstName("Margot")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .updatedAt(LocalDateTime.of(2025, 1, 17, 14, 15, 59))
                .build();

        List<UserDto> userDtos = userMapper.toDto(Arrays.asList(user1, user2));

        assertNotNull(userDtos);
        assertEquals(2, userDtos.size());

        UserDto userDto1 = userDtos.get(0);
        UserDto userDto2 = userDtos.get(1);

        assertEquals(user1.getId(), userDto1.getId());
        assertEquals(user1.getEmail(), userDto1.getEmail());
        assertEquals(user1.getLastName(), userDto1.getLastName());
        assertEquals(user1.getFirstName(), userDto1.getFirstName());
        assertEquals(user1.isAdmin(), userDto1.isAdmin());
        assertEquals(user1.getCreatedAt(), userDto1.getCreatedAt());
        assertEquals(user1.getUpdatedAt(), userDto1.getUpdatedAt());

        assertEquals(user2.getId(), userDto2.getId());
        assertEquals(user2.getEmail(), userDto2.getEmail());
        assertEquals(user2.getLastName(), userDto2.getLastName());
        assertEquals(user2.getFirstName(), userDto2.getFirstName());
        assertEquals(user2.isAdmin(), userDto2.isAdmin());
        assertEquals(user2.getCreatedAt(), userDto2.getCreatedAt());
        assertEquals(user2.getUpdatedAt(), userDto2.getUpdatedAt());
    }
}
