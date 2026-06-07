package com.malaika.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    JwtService jwtService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;

    @InjectMocks
    JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_noAuthHeader_continuesChainWithoutSettingAuth() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUserId(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_authHeaderWithoutBearer_continuesChainWithoutSettingAuth() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUserId(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_validBearerToken_setsAuthenticationInContext() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken123");
        when(jwtService.extractUserId("validtoken123")).thenReturn(1L);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUserId("validtoken123");
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("1", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilterInternal_bearerTokenExtractsOnlyTokenPart() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer my.jwt.token");
        when(jwtService.extractUserId("my.jwt.token")).thenReturn(5L);

        filter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUserId("my.jwt.token");
        assertEquals("5", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilterInternal_authAlreadySet_doesNotOverwriteAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken123");
        when(jwtService.extractUserId("validtoken123")).thenReturn(1L);

        filter.doFilterInternal(request, response, filterChain);
        var firstAuth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(firstAuth);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(2)).doFilter(request, response);
        assertSame(firstAuth, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_nullUserId_doesNotSetAuthentication() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Bearer sometoken");
        when(jwtService.extractUserId("sometoken")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUserId("sometoken");

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}