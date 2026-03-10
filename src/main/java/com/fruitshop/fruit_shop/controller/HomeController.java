package com.fruitshop.fruit_shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fruitshop.fruit_shop.entity.Banner;
import com.fruitshop.fruit_shop.entity.Comment;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.service.BannerService;
import com.fruitshop.fruit_shop.service.CategoryService;
import com.fruitshop.fruit_shop.service.CommentService;
import com.fruitshop.fruit_shop.service.FavoriteService;
import com.fruitshop.fruit_shop.service.ProductService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final BannerService bannerService;
	private final ProductService productService;
	private final CategoryService categoryService;
	private final CommentService commentService;
	private final FavoriteService favoriteService;

	public HomeController(BannerService bannerService, ProductService productService, CategoryService categoryService,
			CommentService commentService, FavoriteService favoriteService) {
		this.bannerService = bannerService;
		this.productService = productService;
		this.categoryService = categoryService;
		this.commentService = commentService;
		this.favoriteService = favoriteService;
	}

	@GetMapping("")
	public String list(Model model) {

		model.addAttribute("sliders", bannerService.findByPosition());
		model.addAttribute("categories", categoryService.findTop4());
		model.addAttribute("products", productService.findAll());

		return "user/home/list";
	}

	@GetMapping("/shop_detail")
	public String shopDetail(@RequestParam("id") Integer id, Model model) {

		Product product = productService.findById(id);

		List<Comment> comments = commentService.findByProduct(id);

		long favoritesCount = favoriteService.countByProduct(id);

		List<Product> relatedProducts = productService.findByCategory(product.getCategory().getId());

		// sản phẩm nổi bật
		List<Product> featuredProducts = productService.getFeaturedProducts();

		model.addAttribute("product", product);
		model.addAttribute("comments", comments);
		model.addAttribute("favoritesCount", favoritesCount);
		model.addAttribute("relatedProducts", relatedProducts);
		model.addAttribute("featuredProducts", featuredProducts);

		return "user/home/detail";
	}
}