package com.example.blog.post;

import java.util.List;

import com.example.blog.post.dto.CreatePostRequest;
import com.example.blog.post.dto.PostResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping
	public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
	}

	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(required = false) String search) {
		return ResponseEntity.ok(postService.getAllPosts(search));
	}
}