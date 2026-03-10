package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findByContentContainingIgnoreCase(String keyword, Pageable pageable);
    List<Comment> findByProductIdAndStatus(Integer productId, Comment.CommentStatus status);

}