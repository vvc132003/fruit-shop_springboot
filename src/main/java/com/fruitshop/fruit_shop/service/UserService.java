package com.fruitshop.fruit_shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.repository.OrderRepository;
import com.fruitshop.fruit_shop.repository.ProductRepository;
import com.fruitshop.fruit_shop.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User login(String email, String password) {

		User user = userRepository.findByEmail(email);

		if (user != null && user.getPassword().equals(password)) {
			return user;
		}

		return null;
	}
}