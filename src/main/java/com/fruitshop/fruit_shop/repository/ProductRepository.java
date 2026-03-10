package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Product> findTop4ByOrderByIdDesc();
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findTop5ByOrderByIdDesc();
}