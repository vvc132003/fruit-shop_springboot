package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.annotation.AdminOnly;
import com.fruitshop.fruit_shop.entity.Order;
import com.fruitshop.fruit_shop.entity.OrderItem;
import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.service.CartService;
import com.fruitshop.fruit_shop.service.OrderItemService;
import com.fruitshop.fruit_shop.service.OrderService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private final CartService cartService;
	private final OrderService orderService;
	private final OrderItemService orderItemService;

	public OrderController(CartService cartService, OrderService orderService, OrderItemService orderItemService) {
		this.cartService = cartService;
		this.orderService = orderService;
		this.orderItemService = orderItemService;
	}

	@AdminOnly
	@GetMapping("")
	public String list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {

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

	@PostMapping("/place")
	public String placeOrder(@RequestParam("shipping_address") String shippingAddress, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/auth/login";
		}

		try {

			orderService.placeOrder(user, shippingAddress);
			return "user/carts/checkoutSuccess";

		} catch (RuntimeException e) {
			session.setAttribute("flash_error", "Có lỗi xảy ra");
			return "redirect:/carts/checkout";
		}
	}

	@GetMapping("history")
	public String history(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, HttpSession session, Model model) {

		int pageSize = 10;

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		Page<Order> pageData;

		if (keyword != null && !keyword.isEmpty()) {
			pageData = orderService.searchByUser(user.getId(), keyword, PageRequest.of(page - 1, pageSize));
		} else {
			pageData = orderService.getByUser(user.getId(), PageRequest.of(page - 1, pageSize));
		}

		model.addAttribute("orders", pageData.getContent());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageData.getTotalPages());
		model.addAttribute("count", pageData.getTotalElements());
		model.addAttribute("keyword", keyword);

		return "user/orders/list";
	}

	@GetMapping("/show")
	public String showOrder(@RequestParam("id") Integer id, Model model) {

		Order order = orderService.findById(id);
		List<OrderItem> items = orderItemService.findByOrderId(id);

		model.addAttribute("order", order);
		model.addAttribute("items", items);

		return "user/orders/show";
	}

	@GetMapping("/orderdetail")
	public String showOrder_s(@RequestParam("id") Integer id, Model model) {

		Order order = orderService.findById(id);
		List<OrderItem> items = orderItemService.findByOrderId(id);

		model.addAttribute("order", order);
		model.addAttribute("items", items);
		model.addAttribute("count", items.size());

		return "admin/orders/orderdetail";
	}

	@GetMapping("/updateStatusQuick")
	public String updateStatusQuick(@RequestParam("id") Integer id) {

		orderService.nextStatus(id);

		return "redirect:/orders";
	}

	@GetMapping("/cancel")
	public String cancelOrder(@RequestParam("id") Integer id) {

		orderService.cancelOrder(id);

		return "redirect:/orders/history";
	}

}