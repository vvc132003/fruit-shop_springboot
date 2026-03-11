package com.fruitshop.fruit_shop.controllerAPI;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fruitshop.fruit_shop.annotation.AdminOnly;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.service.CategoryService;
import com.fruitshop.fruit_shop.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

	private final ProductService productService;
	private final CategoryService categoryService;

	public ProductApiController(ProductService productService, CategoryService categoryService) {
		this.productService = productService;
		this.categoryService = categoryService;
	}
	@AdminOnly
	@GetMapping("")
	public Map<String, Object> list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword) {

		int pageSize = 6;

		Page<Product> pageData;

		if (keyword != null && !keyword.isEmpty()) {
			pageData = productService.search(keyword, PageRequest.of(page - 1, pageSize));
		} else {
			pageData = productService.getAll(PageRequest.of(page - 1, pageSize));
		}

		Map<String, Object> response = new HashMap<>();

		response.put("products", pageData.getContent());
		response.put("page", page);
		response.put("totalPages", pageData.getTotalPages());
		response.put("count", pageData.getTotalElements());
		response.put("keyword", keyword);

		return response;
	}
}
