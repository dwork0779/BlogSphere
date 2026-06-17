package com.example.blog.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull(message = "userId is required") Long userId,
        @NotNull(message = "postId is required") Long postId,
        @NotBlank(message = "comment is required") String comment) {
}
