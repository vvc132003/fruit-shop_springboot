package com.fruitshop.fruit_shop.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fruitshop.fruit_shop.annotation.AdminOnly;
import com.fruitshop.fruit_shop.entity.Category;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.service.CategoryService;
import com.fruitshop.fruit_shop.service.ProductService;

@AdminOnly
@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final CategoryService categoryService;

	public ProductController(ProductService productService, CategoryService categoryService) {
		this.productService = productService;
		this.categoryService = categoryService;
	}

	@AdminOnly
	@GetMapping("")
	public String list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		int pageSize = 6;

		Page<Product> pageData;

		if (keyword != null && !keyword.isEmpty()) {
			pageData = productService.search(keyword, PageRequest.of(page - 1, pageSize));
		} else {
			pageData = productService.getAll(PageRequest.of(page - 1, pageSize));
		}

		model.addAttribute("products", pageData.getContent());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageData.getTotalPages());
		model.addAttribute("count", pageData.getTotalElements());
		model.addAttribute("keyword", keyword);

		return "admin/products/list";
	}

	@AdminOnly
	@GetMapping("/create")
	public String create(Model model) {

		List<Category> categories = categoryService.getAll();

		model.addAttribute("categories", categories);

		return "admin/products/create";
	}

	@AdminOnly
	@PostMapping("/save")
	public String save(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile file,
			@RequestParam("categoryId") Integer categoryId, RedirectAttributes redirectAttributes) {

		try {

			if (!file.isEmpty()) {

				String base64 = Base64.getEncoder().encodeToString(file.getBytes());

				product.setImage(base64);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Category category = categoryService.findById(categoryId);

		product.setCategory(category);

		productService.save(product);
		redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");

		return "redirect:/products";
	}

	@AdminOnly
	@GetMapping("/update")
	public String edit(@RequestParam("id") int id, Model model) {

		Product product = productService.findById(id);

		List<Category> categories = categoryService.getAll();

		model.addAttribute("product", product);
		model.addAttribute("categories", categories);

		return "admin/products/update";
	}

	@AdminOnly
	@PostMapping("/edit")
	public String update(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile file,
			@RequestParam("categoryId") Integer categoryId, RedirectAttributes redirectAttributes) {

		try {

			Product oldProduct = productService.findById(product.getId());

			if (!file.isEmpty()) {

				String base64 = Base64.getEncoder().encodeToString(file.getBytes());

				product.setImage(base64);

			} else {

				product.setImage(oldProduct.getImage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Category category = categoryService.findById(categoryId);

		product.setCategory(category);

		productService.save(product);
		redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");

		return "redirect:/products";
	}

	@AdminOnly
	@GetMapping("/delete")
	public String delete(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		productService.delete(id);
		redirectAttributes.addFlashAttribute("success", "Xoá sản phẩm thành công!");
		return "redirect:/products"; // Redirect to category list after deletion
	}
}
