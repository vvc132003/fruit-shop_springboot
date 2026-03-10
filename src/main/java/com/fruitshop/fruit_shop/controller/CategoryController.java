package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.entity.Category;
import com.fruitshop.fruit_shop.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	// Hiển thị danh sách category
	@GetMapping("")
	public String list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		int pageSize = 10;
		Page<Category> pageData;

		if (keyword != null && !keyword.isEmpty()) {
			pageData = categoryService.search(keyword, PageRequest.of(page - 1, pageSize));
		} else {
			pageData = categoryService.getAll(PageRequest.of(page - 1, pageSize));
		}

		model.addAttribute("categories", pageData.getContent());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageData.getTotalPages());
		model.addAttribute("count", pageData.getTotalElements());
		model.addAttribute("keyword", keyword);

		return "admin/categories/list";
	}

	// Hiển thị form thêm category
	@GetMapping("/create")
	public String createForm(Model model) {
		model.addAttribute("category", new Category());
		return "admin/categories/create";
	}

	// Lưu category
	@PostMapping("/save") // Change this to /create for consistency
	public String save(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
		categoryService.save(category);
		redirectAttributes.addFlashAttribute("success", "Thêm loại sản phẩm thành công!");

		return "redirect:/categories"; // Redirect to category list after saving
	}

	// Xóa category
	@GetMapping("/delete")
	public String delete(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		categoryService.delete(id);
		redirectAttributes.addFlashAttribute("success", "Xoá loại sản phẩm thành công!");
		return "redirect:/categories"; // Redirect to category list after deletion
	}

	@GetMapping("/update")
	public String updateForm(@RequestParam("id") Integer id, Model model) {
		Category category = categoryService.findById(id);
		if (category != null) {
			model.addAttribute("category", category);
			return "admin/categories/update"; // Giao diện cập nhật
		} else {
			// Nếu không tìm thấy category, chuyển hướng về trang danh sách
			return "redirect:/categories";
		}
	}

	// Cập nhật category
	@PostMapping("/edit")
	public String update(@RequestParam("id") Integer id, @ModelAttribute Category category,
			RedirectAttributes redirectAttributes) {
		category.setId(id); // Đảm bảo ID không thay đổi khi gửi lại form
		categoryService.save(category);
		redirectAttributes.addFlashAttribute("success", "Cập nhật loại sản phẩm thành công!");
		return "redirect:/categories"; // Sau khi cập nhật, chuyển hướng về danh sách
	}
}