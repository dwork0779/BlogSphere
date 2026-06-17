package com.example.blog.like.dto;

import jakarta.validation.constraints.NotNull;

public record LikeRequest(
        @NotNull(message = "userId is required") Long userId,
        @NotNull(message = "postId is required") Long postId) {
}
