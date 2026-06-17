package com.example.blog.post;

import java.util.List;

import com.example.blog.comment.CommentService;
import com.example.blog.comment.dto.CommentRequest;
import com.example.blog.comment.dto.CommentResponse;
import com.example.blog.post.dto.CreatePostRequest;
import com.example.blog.post.dto.PostResponse;
import com.example.blog.post.dto.UpdatePostRequest;
import com.example.blog.like.dto.LikeRequest;
import com.example.blog.like.dto.LikeResponse;
import com.example.blog.like.LikeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;
	private final LikeService likeService;
	private final CommentService commentService;

	public PostController(PostService postService, LikeService likeService, CommentService commentService) {
		this.postService = postService;
		this.likeService = likeService;
		this.commentService = commentService;
	}

	@PostMapping
	public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
	}

	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(required = false) String search) {
		return ResponseEntity.ok(postService.getAllPosts(search));
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostResponse> updatePost(
			@PathVariable Long id,
			@Valid @RequestBody UpdatePostRequest request,
			Authentication authentication) {
		return ResponseEntity.ok(postService.updatePost(id, request, authentication.getName()));
	}

	@PostMapping("/like")
	public ResponseEntity<LikeResponse> likePost(@Valid @RequestBody LikeRequest request, Authentication authentication) {
		return ResponseEntity.ok(likeService.likePost(request, authentication.getName()));
	}

	@DeleteMapping("/like")
	public ResponseEntity<Void> unlikePost(@Valid @RequestBody LikeRequest request, Authentication authentication) {
		likeService.unlikePost(request, authentication.getName());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/likes/count")
	public ResponseEntity<Long> getLikeCount(@PathVariable Long id) {
		return ResponseEntity.ok(likeService.countLikesForPost(id));
	}

	@PostMapping("/comment")
	public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest request, Authentication authentication) {
		return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(request, authentication.getName()));
	}

	@GetMapping("/{id}/comments")
	public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id) {
		return ResponseEntity.ok(commentService.getCommentsForPost(id));
	}
}