package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void userDetailsImpl_shouldHaveCorrectValues() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(true)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password", user.getPassword());
        assertTrue(user.getAdmin());
    }

    @Test
    void userDetailsImpl_shouldImplementUserDetailsCorrectly() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(2L)
                .username("user@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("secret")
                .admin(false)
                .build();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(new HashSet<GrantedAuthority>(), authorities);
    }

    @Test
    void equals_shouldCompareOnId() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).username("a").firstName("x").lastName("x").password("x").admin(false).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).username("b").firstName("y").lastName("y").password("y").admin(true).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).username("a").firstName("x").lastName("x").password("x").admin(false).build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }
}
