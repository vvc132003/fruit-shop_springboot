package com.fruitshop.fruit_shop.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fruitshop.fruit_shop.repository.OrderRepository;
import com.fruitshop.fruit_shop.repository.ProductRepository;
import com.fruitshop.fruit_shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	public DashboardService(OrderRepository orderRepository, ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
	}

	public long countProducts() {
		return productRepository.count();
	}

	public long countOrders() {
		return orderRepository.count();
	}

	public double getTotalSales() {
		return orderRepository.sumTotalPrice();
	}

	public double getExpenses() {
		return 5000000; // demo
	}

	public double getRevenueChangePercent() {

		double thisMonth = orderRepository.sumThisMonth();
		double lastMonth = orderRepository.sumLastMonth();

		if (lastMonth == 0)
			return 100;

		return ((thisMonth - lastMonth) / lastMonth) * 100;
	}

	public Map<String, Object> getRevenueChart() {

		List<String> labels = new ArrayList<>();
		List<Long> current = new ArrayList<>();
		List<Long> previous = new ArrayList<>();

		for (int i = 6; i >= 0; i--) {

			LocalDate date = LocalDate.now().minusDays(i);

			labels.add(date.getDayOfMonth() + "/" + date.getMonthValue());

			Long cur = orderRepository.sumByDate(date);
			Long prev = orderRepository.sumByDate(date.minusWeeks(1));

			current.add(cur == null ? 0 : cur);
			previous.add(prev == null ? 0 : prev);
		}

		Map<String, Object> map = new HashMap<>();

		map.put("labels", labels);
		map.put("current", current);
		map.put("previous", previous);

		return map;
	}

	public Map<String, Object> getOrderChart() {

		List<String> labels = new ArrayList<>();
		List<Long> data = new ArrayList<>();

		for (int i = 6; i >= 0; i--) {

			LocalDate date = LocalDate.now().minusDays(i);

			labels.add(date.getDayOfMonth() + "/" + date.getMonthValue());

			data.add(orderRepository.countByDate(date));
		}

		Map<String, Object> map = new HashMap<>();

		map.put("labels", labels);
		map.put("data", data);

		return map;
	}
}