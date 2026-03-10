package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Comment;
import com.fruitshop.fruit_shop.repository.CommentRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<Comment> getAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    public Page<Comment> search(String keyword, Pageable pageable) {
        return commentRepository.findByContentContainingIgnoreCase(keyword, pageable);
    }
    
    public List<Comment> findByProduct(Integer productId){
        return commentRepository
                .findByProductIdAndStatus(productId,
                        Comment.CommentStatus.approved);
    }
}