package com.malaika.backend.jwt;

import com.malaika.backend.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "mysecretkeymysecretkeymysecretkey12";
    private static final long EXPIRATION = 86400000L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
    }

    @Test
    void getSignKey_returnsNonNullSecretKey() throws Exception {
        Method getSignKey = JwtService.class.getDeclaredMethod("getSignKey");
        getSignKey.setAccessible(true);
        Object key = getSignKey.invoke(jwtService);
        assertNotNull(key);
        assertInstanceOf(javax.crypto.SecretKey.class, key);
    }

    @Test
    void generateToken_returnsNonNullNonEmptyToken() {
        String token = jwtService.generateToken(1L);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_differentUserIds_produceDifferentTokens() {
        String token1 = jwtService.generateToken(1L);
        String token2 = jwtService.generateToken(2L);
        assertNotEquals(token1, token2);
    }

    @Test
    void generateToken_hasThreeDotSeparatedParts() {
        String token = jwtService.generateToken(1L);
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void generateToken_containsCorrectUserId() {
        String token = jwtService.generateToken(55L);
        assertEquals(55L, jwtService.extractUserId(token));
    }

    @Test
    void extractUserId_returnsCorrectUserId() {
        String token = jwtService.generateToken(42L);
        assertEquals(42L, jwtService.extractUserId(token));
    }

    @Test
    void extractUserId_largeUserId_extractsCorrectly() {
        String token = jwtService.generateToken(999999L);
        assertEquals(999999L, jwtService.extractUserId(token));
    }

    @Test
    void extractUserId_userIdOne_extractsCorrectly() {
        String token = jwtService.generateToken(1L);
        assertEquals(1L, jwtService.extractUserId(token));
    }

    @Test
    void extractUserId_expiredToken_throwsExpiredJwtException() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String expiredToken = jwtService.generateToken(1L);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);

        assertThrows(ExpiredJwtException.class,
                () -> jwtService.extractUserId(expiredToken));
    }

    @Test
    void isTokenValid_C1_expiredToken_returnsFalse() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String expiredToken = jwtService.generateToken(1L);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);

        assertFalse(jwtService.isTokenValid(expiredToken, 1L));
    }

    @Test
    void isTokenValid_C2_validToken_wrongUser_returnsFalse() {
        String token = jwtService.generateToken(1L);
        assertFalse(jwtService.isTokenValid(token, 2L));
    }

    @Test
    void isTokenValid_C3_validToken_correctUser_returnsTrue() {
        String token = jwtService.generateToken(1L);
        assertTrue(jwtService.isTokenValid(token, 1L));
    }

    @Test
    void isTokenValid_expiredToken_differentUser_returnsFalse() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String expiredToken = jwtService.generateToken(1L);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);

        assertFalse(jwtService.isTokenValid(expiredToken, 99L));
    }

    @Test
    void isTokenExpired_validToken_returnsFalse() throws Exception {
        String token = jwtService.generateToken(1L);

        Method isExpired = JwtService.class
                .getDeclaredMethod("isTokenExpired", String.class);
        isExpired.setAccessible(true);

        boolean result = (boolean) isExpired.invoke(jwtService, token);
        assertFalse(result);
    }

    @Test
    void isTokenExpired_expiredToken_returnsTrue() throws Exception {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String expiredToken = jwtService.generateToken(1L);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);

        Method isExpired = JwtService.class
                .getDeclaredMethod("isTokenExpired", String.class);
        isExpired.setAccessible(true);

        boolean result = (boolean) isExpired.invoke(jwtService, expiredToken);
        assertTrue(result);
    }

    @Test
    void extractAllClaims_validToken_returnsClaimsWithCorrectSubject() throws Exception {
        String token = jwtService.generateToken(7L);

        Method extractAll = JwtService.class
                .getDeclaredMethod("extractAllClaims", String.class);
        extractAll.setAccessible(true);

        io.jsonwebtoken.Claims claims =
                (io.jsonwebtoken.Claims) extractAll.invoke(jwtService, token);

        assertEquals("7", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void extractAllClaims_malformedToken_throwsException() throws Exception {
        Method extractAll = JwtService.class
                .getDeclaredMethod("extractAllClaims", String.class);
        extractAll.setAccessible(true);

        assertThrows(InvocationTargetException.class,
                () -> extractAll.invoke(jwtService, "not.a.valid.jwt"));
    }

    @Test
    void extractAllClaims_expiredToken_throwsExpiredJwtException() throws Exception {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String expiredToken = jwtService.generateToken(1L);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);

        Method extractAll = JwtService.class
                .getDeclaredMethod("extractAllClaims", String.class);
        extractAll.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> extractAll.invoke(jwtService, expiredToken));

        assertInstanceOf(ExpiredJwtException.class, ex.getCause());
    }
}