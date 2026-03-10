package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.PasswordResetCode;
import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.repository.PasswordResetCodeRepository;
import com.fruitshop.fruit_shop.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

	private final PasswordResetCodeRepository repository;
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public PasswordResetService(PasswordResetCodeRepository repository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void createCode(User user, String code) {

		PasswordResetCode reset = new PasswordResetCode();

		reset.setUser(user);
		reset.setCode(code);
		reset.setExpiredAt(LocalDateTime.now().plusMinutes(10));

		repository.save(reset);
	}
    @Transactional
	public boolean resetPassword(String code, String password) {

		Optional<PasswordResetCode> resetOpt = repository.findByCodeAndExpiredAtAfter(code,
				LocalDateTime.now());

		if (resetOpt.isEmpty()) {
			return false;
		}

		PasswordResetCode reset = resetOpt.get();
		User user = reset.getUser();

		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);

		// xóa code sau khi dùng
		repository.deleteByUser(user);

		return true;
	}

}