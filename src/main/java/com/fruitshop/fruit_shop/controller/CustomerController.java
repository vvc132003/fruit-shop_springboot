package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.service.CustomerService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("")
	public String list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		int pageSize = 10;

		Page<User> pageData;

		PageRequest pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

		if (keyword != null && !keyword.isEmpty()) {
			pageData = customerService.searchCustomers(keyword, pageable);
		} else {
			pageData = customerService.getAllCustomers(pageable);
		}

		model.addAttribute("customers", pageData.getContent());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageData.getTotalPages());
		model.addAttribute("count", pageData.getTotalElements());
		model.addAttribute("keyword", keyword);

		return "admin/customers/list";
	}

}