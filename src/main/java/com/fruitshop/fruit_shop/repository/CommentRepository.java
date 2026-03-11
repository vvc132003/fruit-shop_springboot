package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findByContentContainingIgnoreCase(String keyword, Pageable pageable);
    List<Comment> findByProductIdAndStatusIn(Integer productId, List<Comment.CommentStatus> statuses);
    List<Comment> findByUserIdOrderByCreatedAtDesc(Integer userId);
    Optional<Comment> findByIdAndUserId(Integer id, Integer userId);
}