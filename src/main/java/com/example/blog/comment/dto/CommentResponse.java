package com.example.blog.comment.dto;

import java.time.Instant;

public record CommentResponse(Long id, Long userId, Long postId, String comment, Instant createdAt) {
}
