package com.fruitshop.fruit_shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fruitshop.fruit_shop.entity.OrderItem;
import com.fruitshop.fruit_shop.repository.OrderItemRepository;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> findByOrderId(Integer orderId){
        return orderItemRepository.findByOrderId(orderId);
    }

}