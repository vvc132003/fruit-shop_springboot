package com.fruitshop.fruit_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.qos.logback.core.model.Model;

@Controller
@RequestMapping("/products")
public class ProductController {
	@GetMapping("")
	public String listProducts(Model model) {
		return "admin/products/list";
	}
}
