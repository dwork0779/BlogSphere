package com.example.blog.post.dto;

public record PostResponse(Long id, Long userId, String title, String body, Long likeCount) {
}