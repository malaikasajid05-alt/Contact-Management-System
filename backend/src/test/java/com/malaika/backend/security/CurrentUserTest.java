package com.malaika.backend.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    private final CurrentUser currentUser = new CurrentUser();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserId_returnsCorrectId() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "42", null, Collections.emptyList()
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Long userId = currentUser.getCurrentUserId();

        assertEquals(42L, userId);
    }

    @Test
    void getCurrentUserId_differentUserId_returnsCorrectId() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "7", null, Collections.emptyList()
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Long userId = currentUser.getCurrentUserId();

        assertEquals(7L, userId);
    }

    @Test
    void getCurrentUserId_noAuthentication_throwsException() {
        SecurityContextHolder.clearContext();

        assertThrows(Exception.class, currentUser::getCurrentUserId);
    }
}