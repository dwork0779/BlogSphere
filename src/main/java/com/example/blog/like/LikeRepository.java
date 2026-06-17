
package com.example.blog.like;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);

    long countByPostId(Long postId);

}

