package com.fruitshop.fruit_shop.controllerAPI;

import com.fruitshop.fruit_shop.dto.request.CategoryRequest;
import com.fruitshop.fruit_shop.dto.response.*;
import com.fruitshop.fruit_shop.entity.Category;
import com.fruitshop.fruit_shop.mapper.CategoryMapper;
import com.fruitshop.fruit_shop.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {

	private final CategoryService categoryService;

	public CategoryApiController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	// Danh sách category
	@GetMapping
	public ApiResponse<List<CategoryResponse>> list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword) {
		int size = 10;
		Page<Category> pageData;
		if (keyword != null && !keyword.isEmpty()) {
			pageData = categoryService.search(keyword, PageRequest.of(page - 1, size));
		} else {
			pageData = categoryService.getAll(PageRequest.of(page - 1, size));
		}
		List<CategoryResponse> items = pageData.getContent().stream().map(CategoryMapper::toResponse).toList();
		Meta meta = new Meta(page, size, pageData.getTotalElements(), pageData.getTotalPages(),
				pageData.getContent().size());
		return ApiResponse.success("Lấy danh sách category thành công", items, meta);
	}

	// Lấy category theo id
	@GetMapping("/{id}")
	public ApiResponse<CategoryResponse> getById(@PathVariable("id") Integer id) {

		Category category = categoryService.findById(id);

		if (category == null) {
			return new ApiResponse<>(false, "Không tìm thấy category", null, null);
		}

		return ApiResponse.success("Lấy category thành công", CategoryMapper.toResponse(category));
	}

	// Tạo category
	@PostMapping
	public ApiResponse<CategoryResponse> create(@RequestBody CategoryRequest request) {

		Category category = CategoryMapper.toEntity(request);

		Category saved = categoryService.save(category);

		return ApiResponse.success("Thêm loại sản phẩm thành công", CategoryMapper.toResponse(saved));
	}

	// Cập nhật category
	@PutMapping("/{id}")
	public ApiResponse<CategoryResponse> update(@PathVariable("id") Integer id, @RequestBody CategoryRequest request) {

		Category category = CategoryMapper.toEntity(request);
		category.setId(id);

		Category updated = categoryService.save(category);

		return ApiResponse.success("Cập nhật loại sản phẩm thành công", CategoryMapper.toResponse(updated));
	}

	// Xóa category
	@DeleteMapping("/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") Integer id) {

		categoryService.delete(id);

		return ApiResponse.success("Xoá loại sản phẩm thành công", null);
	}
}