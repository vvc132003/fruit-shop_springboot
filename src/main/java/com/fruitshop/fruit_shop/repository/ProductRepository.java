package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}