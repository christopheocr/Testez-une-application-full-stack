package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    @Test
    void testLombokGeneratedMethods() {
        SignupRequest request = new SignupRequest();
        request.setEmail("user@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("mypassword");

        assertEquals("user@example.com", request.getEmail());
        assertEquals("John", request.getFirstName());
        assertEquals("Doe", request.getLastName());
        assertEquals("mypassword", request.getPassword());
    }

    @Test
    void equalsAndHashCode_shouldBehaveCorrectly() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("user@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("mypassword");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("user@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("mypassword");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("another@example.com");
        request3.setFirstName("Jane");
        request3.setLastName("Smith");
        request3.setPassword("otherpassword");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void equalsAndHashCode_shouldBehaveCorrectlyInAllEdgeCases() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("user@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("mypassword");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("user@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("mypassword");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("other@example.com");
        request3.setFirstName("Jane");
        request3.setLastName("Smith");
        request3.setPassword("password123");

        SignupRequest requestWithNullFields1 = new SignupRequest();
        requestWithNullFields1.setEmail(null);
        requestWithNullFields1.setFirstName(null);
        requestWithNullFields1.setLastName(null);
        requestWithNullFields1.setPassword(null);

        SignupRequest requestWithNullFields2 = new SignupRequest();
        requestWithNullFields2.setEmail(null);
        requestWithNullFields2.setFirstName(null);
        requestWithNullFields2.setLastName(null);
        requestWithNullFields2.setPassword(null);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());

        assertEquals(requestWithNullFields1, requestWithNullFields2);
        assertEquals(requestWithNullFields1.hashCode(), requestWithNullFields2.hashCode());

        assertEquals(request1, request1);
        assertNotEquals(request1, null);
        assertNotEquals(request1, "not a SignupRequest");
    }


}
