package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.annotation.AdminOnly;
import com.fruitshop.fruit_shop.entity.Favorite;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.service.FavoriteService;
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
@RequestMapping("/favorites")
public class FavoriteControler {

	private final FavoriteService favoriteService;
	private final ProductService productService;

	public FavoriteControler(FavoriteService favoriteService, ProductService productService) {
		this.productService = productService;
		this.favoriteService = favoriteService;
	}

	@AdminOnly
	@GetMapping("")
	public String list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		int pageSize = 10;

		PageRequest pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

		Page<Favorite> pageData;

		if (keyword != null && !keyword.isEmpty()) {
			pageData = favoriteService.search(keyword, pageable);
		} else {
			pageData = favoriteService.getAll(pageable);
		}

		model.addAttribute("favorites", pageData.getContent());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageData.getTotalPages());
		model.addAttribute("count", pageData.getTotalElements());
		model.addAttribute("keyword", keyword);

		return "admin/favorites/list";
	}

	@GetMapping("/toggle")
	public String toggleFavorite(@RequestParam("product_id") Integer productId, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}

		Favorite favorite = favoriteService.findByUserAndProduct(user.getId(), productId);

		if (favorite != null) {
			favoriteService.delete(favorite); // bỏ thích
		} else {

			Product product = productService.findById(productId);

			Favorite f = new Favorite();
			f.setUser(user);
			f.setProduct(product);
			f.setCreatedAt(LocalDateTime.now());

			favoriteService.save(f);
		}

		return "redirect:/shop_detail?id=" + productId;
	}
	
	@GetMapping("/history")
	public String history(HttpSession session, Model model){

	    User user = (User) session.getAttribute("user");

	    if(user == null){
	        return "redirect:/login";
	    }

	    List<Favorite> favorites = favoriteService.findByUser(user.getId());

	    model.addAttribute("favorites", favorites);

	    return "user/favorites/history";
	}
}