package com.example.inventory.security;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "revoked_tokens", indexes = {@Index(name = "idx_revoked_exp", columnList = "expiry")})
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000, nullable = false)
    private String token;
    private Instant expiry;

    public RevokedToken() {
    }

    public RevokedToken(String token, Instant expiry) {
        this.token = token;
        this.expiry = expiry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }
}
