package com.fruitshop.fruit_shop.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping("")
	public String list(Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		var items = cartService.getItems(user);

		model.addAttribute("items", items);
		model.addAttribute("totalQuantity", cartService.getTotalQuantity(user));
		model.addAttribute("totalPrice", cartService.getTotalPrice(user));

		return "user/carts/list";
	}

	@GetMapping("/checkout")
	public String checkout(Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		var items = cartService.getItems(user);

		model.addAttribute("items", items);
		model.addAttribute("totalQuantity", cartService.getTotalQuantity(user));
		model.addAttribute("totalPrice", cartService.getTotalPrice(user));

		return "user/carts/checkout";
	}

	@GetMapping("/checkoutSuccess")
	public String checkoutSuccess() {
		return "user/carts/checkoutSuccess";
	}

}
