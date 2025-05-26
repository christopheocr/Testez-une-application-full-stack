package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey123456");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 10000); // 10 secondes
    }

    @Test
    void generateAndValidateToken_shouldWorkCorrectly() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user@example.com", "John", "Doe", false, "pwd");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("user@example.com", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_onExpiredToken() {
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("user@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(SignatureAlgorithm.HS512, "testSecretKey123456")
                .compact();

        boolean result = jwtUtils.validateJwtToken(expiredToken);
        assertFalse(result);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_onMalformedToken() {
        String malformedToken = "this.is.not.a.valid.token";
        boolean result = jwtUtils.validateJwtToken(malformedToken);
        assertFalse(result);
    }

    @Test
    void testMalformedToken() {
        String malformedToken = "abc.def"; // non base64 -> MalformedJwtException
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    void testUnsupportedToken() {
        String unsupportedToken = Jwts.builder()
                .setPayload("{\"unsupported\":true}")
                .signWith(SignatureAlgorithm.HS512, "testSecretKey123456789012345678901234567890")
                .compact();
        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }

    @Test
    void testEmptyToken() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
    }

}
