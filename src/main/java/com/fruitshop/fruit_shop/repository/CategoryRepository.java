package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Category> findTop4ByOrderByIdDesc();
}