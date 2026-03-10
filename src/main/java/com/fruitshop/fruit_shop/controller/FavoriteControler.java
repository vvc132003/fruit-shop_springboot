package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.entity.Favorite;
import com.fruitshop.fruit_shop.service.FavoriteService;

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

	public FavoriteControler(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

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
}