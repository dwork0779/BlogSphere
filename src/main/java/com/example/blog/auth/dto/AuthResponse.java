package com.example.blog.auth.dto;

public record AuthResponse(Long userId, String token, String username) {
}