package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.annotation.AdminOnly;
import com.fruitshop.fruit_shop.entity.Comment;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.service.CommentService;
import com.fruitshop.fruit_shop.service.ProductService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

	private final CommentService commentService;
	private final ProductService productService;

	public CommentController(CommentService commentService, ProductService productService) {
		this.commentService = commentService;
		this.productService = productService;
	}

	@AdminOnly
	@GetMapping("")
	public String list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		int pageSize = 10;

		PageRequest pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

		Page<Comment> pageData;

		if (keyword != null && !keyword.isEmpty()) {
			pageData = commentService.search(keyword, pageable);
		} else {
			pageData = commentService.getAll(pageable);
		}

		model.addAttribute("comments", pageData.getContent());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageData.getTotalPages());
		model.addAttribute("count", pageData.getTotalElements());
		model.addAttribute("keyword", keyword);

		return "admin/comments/list";
	}

	@PostMapping("/store")
	public String storeComment(@RequestParam("productId") Integer productId, @RequestParam("content") String content,
			HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		Product product = productService.findById(productId);

		Comment comment = new Comment();
		comment.setContent(content);
		comment.setProduct(product);
		comment.setUser(user);
		comment.setCreatedAt(LocalDateTime.now());

		// chờ admin duyệt
		comment.setStatus(Comment.CommentStatus.pending);

		commentService.save(comment);

		return "redirect:/shop_detail?id=" + productId;
	}

	@GetMapping("/updateStatus")
	public String updateStatus(@RequestParam("id") Integer id, @RequestParam("status") String status) {

		Comment.CommentStatus newStatus = Comment.CommentStatus.valueOf(status);
		commentService.updateStatus(id, newStatus);

		return "redirect:/comments";
	}

	@GetMapping("/history")
	public String history(HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		List<Comment> comments = commentService.findByUser(user.getId());

		model.addAttribute("comments", comments);

		return "user/comments/history";
	}

	@GetMapping("/delete")
	public String deleteComment(@RequestParam("id") Integer id, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		commentService.deleteComment(id, user.getId());

		return "redirect:/comments/history";
	}

}