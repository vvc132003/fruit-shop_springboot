package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.repository.ProductRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// Lấy tất cả có phân trang
	public Page<Product> getAll(Pageable pageable) {
		// Thêm sắp xếp theo createdAt giảm dần (mới nhất trước)
		Pageable sortedByDate = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Order.desc("id")));
		return productRepository.findAll(sortedByDate);
	}

	// Tìm kiếm theo tên
	public Page<Product> search(String keyword, Pageable pageable) {
		// Thêm sắp xếp theo createdAt giảm dần (mới nhất trước)
		Pageable sortedByDate = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Order.desc("id")));
		return productRepository.findByNameContainingIgnoreCase(keyword, sortedByDate);
	}

	// Lấy product theo id
	public Product getById(Integer id) {
		return productRepository.findById(id).orElse(null);
	}

	public Product findById(int id) {

		return productRepository.findById(id).orElse(null);

	}

	// Lưu product
	public Product save(Product product) {
		return productRepository.save(product);
	}

	// Xóa product
	public void delete(Integer id) {
		productRepository.deleteById(id);
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public List<Product> findByCategory(Integer categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	public List<Product> getFeaturedProducts() {
		return productRepository.findTop5ByOrderByIdDesc();
	}

	public Page<Product> getProducts(Integer categoryId, String keyword, String sort, int page, int size) {

		Sort sorting = Sort.unsorted();

		if ("new".equals(sort)) {
			sorting = Sort.by("createdAt").descending();
		} else if ("price_asc".equals(sort)) {
			sorting = Sort.by("price").ascending();
		} else if ("price_desc".equals(sort)) {
			sorting = Sort.by("price").descending();
		} else if ("featured".equals(sort)) {
			sorting = Sort.by("isFeatured").descending();
		}

		Pageable pageable = PageRequest.of(page - 1, size, sorting);

		if (categoryId != null && keyword != null && !keyword.isEmpty()) {
			return productRepository.findByCategory_IdAndNameContainingIgnoreCase(categoryId, keyword, pageable);
		}

		if (categoryId != null) {
			return productRepository.findByCategory_Id(categoryId, pageable);
		}

		if (keyword != null && !keyword.isEmpty()) {
			return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
		}

		return productRepository.findAll(pageable);
	}

}