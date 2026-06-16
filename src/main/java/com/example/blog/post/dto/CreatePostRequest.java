package com.example.blog.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(
		@NotNull(message = "userId is required") Long userId,
		@NotBlank(message = "title is required") String title,
		@NotBlank(message = "body is required") String body) {
}