package com.example.inventory.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        public String username;

        @NotBlank
        @Email
        public String email;

        @NotBlank
        @Size(min = 6, max = 100)
        public String password;

        public String role; // ADMIN / USER
    }

    public static class LoginRequest {
        @NotBlank
        public String username;

        @NotBlank
        public String password;
    }

    public static class AuthResponse {
        public String token;

        public AuthResponse(String token) {
            this.token = token;
        }
    }

    public static class MessageResponse {
        public String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }
}
