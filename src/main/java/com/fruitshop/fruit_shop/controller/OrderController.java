package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.entity.Order;
import com.fruitshop.fruit_shop.service.OrderService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       Model model) {

        int pageSize = 10;

        Page<Order> pageData;

        if (keyword != null && !keyword.isEmpty()) {
            pageData = orderService.search(keyword, PageRequest.of(page - 1, pageSize));
        } else {
            pageData = orderService.getAll(PageRequest.of(page - 1, pageSize));
        }

        model.addAttribute("orders", pageData.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("count", pageData.getTotalElements());
        model.addAttribute("keyword", keyword);

        return "admin/orders/list";
    }

}