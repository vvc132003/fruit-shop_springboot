package com.fruitshop.fruit_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fruitshop.fruit_shop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(Integer orderId);

}