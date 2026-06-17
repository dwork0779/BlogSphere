package com.example.blog.comment;

import com.example.blog.comment.dto.CommentRequest;
import com.example.blog.comment.dto.CommentResponse;
import com.example.blog.post.PostRepository;
import com.example.blog.user.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public CommentResponse addComment(CommentRequest request, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!user.getId().equals(request.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "userId does not match authenticated user");
        }

        if (!postRepository.existsById(request.postId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        Comment comment = Comment.builder()
                .userId(request.userId())
                .postId(request.postId())
                .comment(request.comment().trim())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponse(savedComment.getId(), savedComment.getUserId(), savedComment.getPostId(), savedComment.getComment(), savedComment.getCreatedAt());
    }

    public List<CommentResponse> getCommentsForPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getUserId(), comment.getPostId(), comment.getComment(), comment.getCreatedAt()))
                .toList();
    }
}
