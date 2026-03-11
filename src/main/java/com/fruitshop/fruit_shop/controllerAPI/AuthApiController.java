package com.fruitshop.fruit_shop.controllerAPI;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.entity.User.Role;
import com.fruitshop.fruit_shop.entity.User.Status;
import com.fruitshop.fruit_shop.service.MailService;
import com.fruitshop.fruit_shop.service.PasswordResetService;
import com.fruitshop.fruit_shop.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	@Autowired
	private PasswordResetService passwordResetService;

	// LOGIN
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) {

		User user = userService.login(email, password);

		if (user == null) {
			return ResponseEntity.badRequest()
					.body(Map.of("status", "error", "message", "Email hoặc mật khẩu không đúng"));
		}

		session.setAttribute("user", user);

		return ResponseEntity
				.ok(Map.of("status", "success", "message", "Đăng nhập thành công", "role", user.getRole()));
	}

	// LOGOUT
	@PostMapping("/logout")
	public Map<String, Object> logout(HttpSession session) {

		session.invalidate();

		return Map.of("status", "success", "message", "Đăng xuất thành công");
	}

	// CHANGE PASSWORD
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestParam("current_password") String currentPassword,
			@RequestParam("new_password") String newPassword, @RequestParam("confirm_password") String confirmPassword,
			HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return ResponseEntity.status(401).body(Map.of("message", "Chưa đăng nhập"));
		}

		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu hiện tại không đúng"));
		}

		if (!newPassword.equals(confirmPassword)) {
			return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu xác nhận không khớp"));
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userService.save(user);

		return ResponseEntity.ok(Map.of("status", "success", "message", "Cập nhật mật khẩu thành công"));
	}

	// REGISTER
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword) {

		if (!password.equals(confirmPassword)) {
			return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu không khớp"));
		}

		if (userService.existsByEmail(email)) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email đã tồn tại"));
		}

		User user = new User();
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setCreatedAt(LocalDateTime.now());
		user.setRole(Role.user);
		user.setStatus(Status.active);

		userService.save(user);

		return ResponseEntity.ok(Map.of("status", "success", "message", "Đăng ký thành công"));
	}

	// SEND RESET CODE
	@PostMapping("/forgot-password")
	public ResponseEntity<?> sendResetCode(@RequestParam("email") String email) {

		User user = userService.findByEmail(email).orElse(null);

		if (user == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại"));
		}

		String code = String.valueOf((int) (Math.random() * 900000) + 100000);

		passwordResetService.createCode(user, code);

		mailService.sendResetPasswordMail(email, code);

		return ResponseEntity.ok(Map.of("status", "success", "message", "Đã gửi mã xác nhận"));
	}

	// RESET PASSWORD
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam("code") String code, @RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword) {

		if (!password.equals(confirmPassword)) {
			return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu không khớp"));
		}

		boolean success = passwordResetService.resetPassword(code, password);

		if (!success) {
			return ResponseEntity.badRequest().body(Map.of("message", "Mã xác nhận không hợp lệ hoặc đã hết hạn"));
		}

		return ResponseEntity.ok(Map.of("status", "success", "message", "Đổi mật khẩu thành công"));
	}
}