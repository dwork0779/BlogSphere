package com.example.blog.like;

import com.example.blog.like.dto.LikeRequest;
import com.example.blog.like.dto.LikeResponse;
import com.example.blog.post.PostRepository;
import com.example.blog.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public LikeResponse likePost(LikeRequest request, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!user.getId().equals(request.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "userId does not match authenticated user");
        }

        if (!postRepository.existsById(request.postId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        if (likeRepository.existsByUserIdAndPostId(request.userId(), request.postId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already liked");
        }

        Like like = Like.builder()
                .userId(request.userId())
                .postId(request.postId())
                .build();

        Like saved = likeRepository.save(like);
        return new LikeResponse(saved.getId(), saved.getUserId(), saved.getPostId());
    }

    @Transactional
    public void unlikePost(LikeRequest request, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!user.getId().equals(request.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "userId does not match authenticated user");
        }

        likeRepository.findByUserIdAndPostId(request.userId(), request.postId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found"));

        likeRepository.deleteByUserIdAndPostId(request.userId(), request.postId());
    }

    public long countLikesForPost(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
