package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Comment;
import com.fruitshop.fruit_shop.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

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

	public List<Comment> findByProduct(Integer productId) {

		return commentRepository.findByProductIdAndStatusIn(productId,
				List.of(Comment.CommentStatus.pending, Comment.CommentStatus.approved));

	}

	public void save(Comment comment) {
		commentRepository.save(comment);
	}

	public void updateStatus(Integer id, Comment.CommentStatus status) {

		Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

		comment.setStatus(status);

		commentRepository.save(comment);

	}

	public List<Comment> findByUser(Integer userId) {
		return commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	public void deleteComment(Integer commentId, Integer userId) {

		Optional<Comment> comment = commentRepository.findByIdAndUserId(commentId, userId);

		if (comment.isPresent()) {
			commentRepository.delete(comment.get());
		}

	}
}