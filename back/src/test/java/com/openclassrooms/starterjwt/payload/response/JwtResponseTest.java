package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtResponseTest {

    @Test
    void testJwtResponseConstructorAndGetters() {
        JwtResponse jwtResponse = new JwtResponse(
                "test-token", 1L, "username", "first", "last", true
        );

        assertEquals("test-token", jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("username", jwtResponse.getUsername());
        assertEquals("first", jwtResponse.getFirstName());
        assertEquals("last", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
    }
}
