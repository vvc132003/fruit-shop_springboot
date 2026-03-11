package com.fruitshop.fruit_shop.controllerAPI;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.service.CartService;
import com.fruitshop.fruit_shop.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

	private final CartService cartService;
	private final UserService userService;

	public CartApiController(CartService cartService, UserService userService) {
		this.cartService = cartService;
		this.userService = userService;
	}

	@PostMapping("/add")
	@ResponseBody
	public Map<String, Object> addToCart(@RequestParam("id") Integer id, HttpSession session) {

		Map<String, Object> res = new HashMap<>();

		User user = (User) session.getAttribute("user");

		if (user == null) {
			res.put("status", "error");
			res.put("message", "Chưa đăng nhập");
			return res;
		}

		cartService.addProduct(user.getId(), id);

		res.put("status", "success");
		res.put("message", "Đã thêm vào giỏ hàng");

		return res;
	}

	@PostMapping("/remove")
	public Map<String, Object> removeItem(@RequestParam("id") Integer id, HttpSession session) {

		Map<String, Object> res = new HashMap<>();

		User user = (User) session.getAttribute("user");

		if (user == null) {
			res.put("status", "error");
			res.put("message", "Chưa đăng nhập");
			return res;
		}

		cartService.removeItem(user.getId(), id);

		res.put("status", "success");
		res.put("message", "Đã xóa sản phẩm");

		return res;
	}

	@PostMapping("/increase")
	public Map<String, Object> increase(@RequestParam("id") Integer id, HttpSession session) {

		Map<String, Object> res = new HashMap<>();

		User user = (User) session.getAttribute("user");

		if (user == null) {
			res.put("status", "error");
			res.put("message", "Chưa đăng nhập");
			return res;
		}

		cartService.increase(user.getId(), id);

		res.put("status", "success");

		return res;
	}

	@PostMapping("/decrease")
	public Map<String, Object> decrease(@RequestParam("id") Integer id, HttpSession session) {

		Map<String, Object> res = new HashMap<>();

		User user = (User) session.getAttribute("user");

		if (user == null) {
			res.put("status", "error");
			res.put("message", "Chưa đăng nhập");
			return res;
		}

		cartService.decrease(user.getId(), id);

		res.put("status", "success");

		return res;
	}
}