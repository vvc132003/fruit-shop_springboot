package com.fruitshop.fruit_shop.config;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.service.CartService;
import com.fruitshop.fruit_shop.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalModel {

	private final CartService cartService;
	private final UserService userService;

	public GlobalModel(CartService cartService, UserService userService) {
		this.cartService = cartService;
		this.userService = userService;
	}

	@ModelAttribute("cartCount")
	public int cartCount(HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return 0;
		}

		return cartService.countByUser(user.getId());
	}

}