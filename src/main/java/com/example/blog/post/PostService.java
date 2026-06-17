package com.example.blog.post;

import java.util.List;

import com.example.blog.post.dto.CreatePostRequest;
import com.example.blog.post.dto.PostResponse;
import com.example.blog.post.dto.UpdatePostRequest;
import com.example.blog.user.UserRepository;
import com.example.blog.like.LikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.likeRepository = likeRepository;
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
		long likeCount = likeRepository.countByPostId(savedPost.getId());
		return new PostResponse(savedPost.getId(), savedPost.getUserId(), savedPost.getTitle(), savedPost.getBody(), likeCount);
	}

	public List<PostResponse> getAllPosts(String search) {
		List<Post> posts = search == null || search.isBlank()
				? postRepository.findAllByOrderByIdDesc()
				: postRepository.findByBodyContainingOrderByIdDesc(search.trim());

		return posts.stream()
			.map(post -> new PostResponse(post.getId(), post.getUserId(), post.getTitle(), post.getBody(), likeRepository.countByPostId(post.getId())))
				.toList();
	}

	public PostResponse updatePost(Long postId, UpdatePostRequest request, String username) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

		Long currentUserId = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"))
				.getId();

		if (!currentUserId.equals(post.getUserId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own posts");
		}

		post.setTitle(request.title());
		post.setBody(request.body());

		Post savedPost = postRepository.save(post);
		long likeCount = likeRepository.countByPostId(savedPost.getId());
		return new PostResponse(savedPost.getId(), savedPost.getUserId(), savedPost.getTitle(), savedPost.getBody(), likeCount);
	}
}