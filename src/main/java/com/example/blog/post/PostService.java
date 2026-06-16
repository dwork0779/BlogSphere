package com.example.blog.post;

import java.util.List;

import com.example.blog.post.dto.CreatePostRequest;
import com.example.blog.post.dto.PostResponse;
import com.example.blog.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	public PostResponse createPost(CreatePostRequest request) {
		userRepository.findById(request.userId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Post post = Post.builder()
				.title(request.title())
				.userId(request.userId())
				.body(request.body())
				.build();

		Post savedPost = postRepository.save(post);
		return new PostResponse(savedPost.getId(), savedPost.getUserId(), savedPost.getTitle(), savedPost.getBody());
	}

	public List<PostResponse> getAllPosts(String search) {
		List<Post> posts = search == null || search.isBlank()
				? postRepository.findAllByOrderByIdDesc()
				: postRepository.findByBodyContainingOrderByIdDesc(search.trim());

		return posts.stream()
				.map(post -> new PostResponse(post.getId(), post.getUserId(), post.getTitle(), post.getBody()))
				.toList();
	}
}