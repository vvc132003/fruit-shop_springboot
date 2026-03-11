package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.CartItem;
import com.fruitshop.fruit_shop.entity.Order;
import com.fruitshop.fruit_shop.entity.OrderItem;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.repository.CartItemRepository;
import com.fruitshop.fruit_shop.repository.OrderItemRepository;
import com.fruitshop.fruit_shop.repository.OrderRepository;
import com.fruitshop.fruit_shop.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private final CartItemRepository cartItemRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	private final MailService mailService;

	public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository,
			OrderItemRepository orderItemRepository, ProductRepository productRepository, MailService mailService) {

		this.cartItemRepository = cartItemRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.mailService = mailService;
	}

	public Page<Order> getAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	public Page<Order> search(String keyword, Pageable pageable) {
		return orderRepository.findByReceiverNameContainingIgnoreCase(keyword, pageable);
	}

	public Page<Order> getByUser(Integer userId, Pageable pageable) {
		return orderRepository.findByUserId(userId, pageable);
	}

	public Page<Order> searchByUser(Integer userId, String keyword, Pageable pageable) {
		return orderRepository.searchByUser(userId, keyword, pageable);
	}

	public Order findById(Integer id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Transactional
	public void cancelOrder(Integer orderId) {

		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		// chỉ huỷ khi pending
		if (order.getStatus() != Order.OrderStatus.pending) {
			throw new RuntimeException("Không thể huỷ đơn này");
		}

		// hoàn stock
		for (OrderItem item : order.getItems()) {

			Product product = item.getProduct();

			product.setStock(product.getStock() + item.getQuantity());

			productRepository.save(product);
		}

		// đổi trạng thái
		order.setStatus(Order.OrderStatus.cancelled);

		orderRepository.save(order);
	}

	public boolean nextStatus(Integer id) {

		Order order = orderRepository.findById(id).orElse(null);

		if (order == null) {
			return false;
		}

		Order.OrderStatus current = order.getStatus();

		switch (current) {

		case pending:
			order.setStatus(Order.OrderStatus.shipping);
			break;

		case shipping:
			order.setStatus(Order.OrderStatus.completed);
			break;

		default:
			return false; // completed hoặc cancelled thì không đổi
		}

		orderRepository.save(order);
		return true;
	}

	@Transactional
	public void placeOrder(User user, String shippingAddress) {

		if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
			throw new RuntimeException("ADDRESS_INVALID");
		}

		List<CartItem> items = cartItemRepository.findByCart_User(user);

		if (items.isEmpty()) {
			throw new RuntimeException("CART_EMPTY");
		}

		double subtotal = 0;

		for (CartItem item : items) {
			subtotal += item.getProduct().getPrice() * item.getQuantity();
		}

		Order order = new Order();

		order.setUser(user);
		order.setReceiverName(user.getFirstName() + " " + user.getLastName());
		order.setReceiverPhone(user.getPhone());
		order.setShippingAddress(shippingAddress);
		order.setSubtotal(subtotal);
		order.setDiscount(0.0);
		order.setTotal(subtotal);
		order.setStatus(Order.OrderStatus.pending);
		order.setCreatedAt(LocalDateTime.now());

		orderRepository.save(order);
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem item : items) {

			Product product = item.getProduct();

			if (product.getStock() < item.getQuantity()) {
				throw new RuntimeException("OUT_OF_STOCK");
			}

			product.setStock(product.getStock() - item.getQuantity());
			productRepository.save(product);

			OrderItem orderItem = new OrderItem();

			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(item.getQuantity());
			orderItem.setPrice(product.getPrice());

			orderItemRepository.save(orderItem);
			orderItems.add(orderItem);
		}
		mailService.sendOrderSuccess(user.getEmail(), user.getFirstName() + " " + user.getLastName(), order.getId(),
				orderItems, order.getTotal());
		cartItemRepository.deleteAll(items);
	}
}