package com.fruitshop.fruit_shop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.repository.OrderRepository;
import com.fruitshop.fruit_shop.repository.ProductRepository;
import com.fruitshop.fruit_shop.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User login(String email, String rawPassword) {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (passwordEncoder.matches(rawPassword, user.getPassword())) {
				return user; // đúng mật khẩu
			}
		}
		return null; // sai
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}