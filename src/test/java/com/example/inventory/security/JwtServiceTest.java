package com.example.inventory.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    @Test
    void generateAndValidate() {
        JwtService svc = new JwtService();
        try {
            var f1 = JwtService.class.getDeclaredField("secret");
            f1.setAccessible(true);
            f1.set(svc, "YWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWE="); // base64 of 32 bytes
            var f2 = JwtService.class.getDeclaredField("expirationMinutes");
            f2.setAccessible(true);
            f2.set(svc, 5L);
        } catch (Exception e) {
            fail(e);
        }
        String token = svc.generateToken("alice", "USER");
        assertTrue(svc.isTokenValid(token));
        assertEquals("alice", svc.extractUsername(token));
    }
}
