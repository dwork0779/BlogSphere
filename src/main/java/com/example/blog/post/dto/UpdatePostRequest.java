package com.example.blog.post.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest(
		@NotBlank(message = "title is required") String title,
		@NotBlank(message = "body is required") String body) {
}