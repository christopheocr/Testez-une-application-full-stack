package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;

public class AuthEntryPointJwtTest {
    @Test
    void testCommenceSetsUnauthorizedResponse() throws IOException, ServletException {
        AuthEntryPointJwt entryPoint = new AuthEntryPointJwt();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/test");

        MockHttpServletResponse response = new MockHttpServletResponse();

        AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn("Unauthorized access");

        entryPoint.commence(request, response, exception);

        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = mapper.readValue(response.getContentAsByteArray(), Map.class);

        assertEquals(401, body.get("status"));
        assertEquals("Unauthorized", body.get("error"));
        assertEquals("Unauthorized access", body.get("message"));
        assertEquals("/api/test", body.get("path"));
    }
}
