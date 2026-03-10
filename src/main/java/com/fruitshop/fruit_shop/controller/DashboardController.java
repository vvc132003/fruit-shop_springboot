package com.fruitshop.fruit_shop.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.service.CommentService;
import com.fruitshop.fruit_shop.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("")
	public String list(Model model) {

		model.addAttribute("totalSales", dashboardService.getTotalSales());
		model.addAttribute("changePercent", dashboardService.getRevenueChangePercent());
		model.addAttribute("countP", dashboardService.countProducts());
		model.addAttribute("totalOrders", dashboardService.countOrders());
		model.addAttribute("expenses", dashboardService.getExpenses());

		return "admin/dashboard/list";
	}

}
