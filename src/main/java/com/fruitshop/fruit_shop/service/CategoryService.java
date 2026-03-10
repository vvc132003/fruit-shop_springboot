package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Category;
import com.fruitshop.fruit_shop.repository.CategoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public static String toSlug(String input) {
		String slug = Normalizer.normalize(input, Normalizer.Form.NFD);
		slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		slug = slug.toLowerCase();
		slug = slug.replaceAll("[^a-z0-9\\s-]", "");
		slug = slug.replaceAll("\\s+", "-");
		slug = slug.replaceAll("-+", "-");

		return slug;
	}

	public List<Category> getAll() {
		return categoryRepository.findAll();
	}

	public Category save(Category category) {
		String slug = toSlug(category.getName());
		category.setSlug(slug);
		return categoryRepository.save(category);
	}

	public void delete(Integer id) {
		try {
			// Kiểm tra xem category có tồn tại không trước khi xóa
			if (categoryRepository.existsById(id)) {
				categoryRepository.deleteById(id);
			} else {
				throw new IllegalArgumentException("Category with id " + id + " not found");
			}
		} catch (Exception e) {
			// Log lỗi chi tiết
			e.printStackTrace();
			throw new RuntimeException("Error deleting category", e);
		}
	}

	public Page<Category> getAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	public Page<Category> search(String keyword, Pageable pageable) {
		return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
	}

	public Category findById(Integer id) {
		Optional<Category> category = categoryRepository.findById(id);
		return category.orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found"));
	}

	public List<Category> findTop4() {
		return categoryRepository.findTop4ByOrderByIdDesc();
	}
}