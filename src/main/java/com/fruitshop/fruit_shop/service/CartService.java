package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.*;
import com.fruitshop.fruit_shop.repository.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			ProductRepository productRepository, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	public List<CartItem> getItems(User user) {

		Cart cart = cartRepository.findByUser(user).orElse(null);

		if (cart == null) {
			return List.of();
		}

		return cartItemRepository.findByCart(cart);
	}

	public int getTotalQuantity(User user) {

		return getItems(user).stream().mapToInt(CartItem::getQuantity).sum();
	}

	public double getTotalPrice(User user) {

		return getItems(user).stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
	}

	public int countByUser(Integer userId) {

		Integer total = cartItemRepository.countByUser(userId);

		return total == null ? 0 : total;
	}

	public void addProduct(Integer userId, Integer productId) {

		User user = userRepository.findById(userId).orElseThrow();
		Product product = productRepository.findById(productId).orElseThrow();
		if (product.getStock() <= 0) {
			throw new RuntimeException("Sản phẩm đã hết hàng");
		}

		// tìm cart của user
		Cart cart = cartRepository.findByUser(user).orElse(null);

		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			cart.setCreatedAt(LocalDateTime.now());
			cartRepository.save(cart);
		}

		// kiểm tra product đã có trong cart chưa
		CartItem item = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

		if (item != null) {

			item.setQuantity(item.getQuantity() + 1);
			cartItemRepository.save(item);

		} else {

			CartItem newItem = new CartItem();
			newItem.setCart(cart);
			newItem.setProduct(product);
			newItem.setQuantity(1);
			newItem.setPrice(product.getPrice());
			newItem.setCreatedAt(LocalDateTime.now());

			cartItemRepository.save(newItem);
		}
		product.setStock(product.getStock() - 1);
		productRepository.save(product);
	}

	public void removeItem(Integer userId, Integer cartItemId) {

		CartItem item = cartItemRepository.findById(cartItemId).orElseThrow();

		if (!item.getCart().getUser().getId().equals(userId)) {
			throw new RuntimeException("Không hợp lệ");
		}

		Product product = item.getProduct();

		// trả lại stock
		product.setStock(product.getStock() + item.getQuantity());
		productRepository.save(product);

		cartItemRepository.delete(item);
	}

	public void increase(Integer userId, Integer cartItemId) {

		CartItem item = cartItemRepository.findById(cartItemId).orElseThrow();

		Product product = item.getProduct();

		if (product.getStock() <= 0) {
			throw new RuntimeException("Hết hàng");
		}

		item.setQuantity(item.getQuantity() + 1);
		cartItemRepository.save(item);

		// giảm stock
		product.setStock(product.getStock() - 1);
		productRepository.save(product);
	}

	public void decrease(Integer userId, Integer cartItemId) {

		Optional<CartItem> optional = cartItemRepository.findById(cartItemId);

		if (optional.isEmpty()) {
			throw new RuntimeException("Cart item không tồn tại");
		}

		CartItem item = optional.get();

		Product product = item.getProduct();

		if (item.getQuantity() > 1) {

			item.setQuantity(item.getQuantity() - 1);
			cartItemRepository.save(item);

			product.setStock(product.getStock() + 1);
			productRepository.save(product);

		} else {

			product.setStock(product.getStock() + 1);
			productRepository.save(product);

			cartItemRepository.delete(item);
		}
	}
}