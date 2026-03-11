package com.fruitshop.fruit_shop.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	public User findById(Integer id) {
		return userRepository.findById(id).orElse(null);
	}

	public void updateProfile(Integer id, String firstName, String lastName, String email, String phone, String address,
			MultipartFile avatar) throws Exception {

		User user = userRepository.findById(id).orElse(null);

		if (user == null)
			return;

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPhone(phone);
		user.setAddress(address);

		// nếu có upload ảnh
		if (avatar != null && !avatar.isEmpty()) {

			byte[] bytes = avatar.getBytes();

			String base64 = Base64.getEncoder().encodeToString(bytes);

			user.setAvatar(base64);
		}

		userRepository.save(user);
	}

	public void changePassword(Integer userId, String currentPassword, String newPassword, String confirmPassword) {

		if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
			throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
		}

		if (!newPassword.equals(confirmPassword)) {
			throw new RuntimeException("Mật khẩu mới không khớp");
		}

		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			throw new RuntimeException("User không tồn tại");
		}

		// kiểm tra password hiện tại
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new RuntimeException("Mật khẩu hiện tại không đúng");
		}

		// update password
		user.setPassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);
	}
}