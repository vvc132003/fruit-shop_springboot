package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Order;
import com.fruitshop.fruit_shop.repository.OrderRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> search(String keyword, Pageable pageable) {
        return orderRepository.findByReceiverNameContainingIgnoreCase(keyword, pageable);
    }

}