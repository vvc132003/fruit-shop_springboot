package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.repository.OrderRepository;
import com.fruitshop.fruit_shop.repository.ProductRepository;
import com.fruitshop.fruit_shop.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	// Trang login
	@GetMapping("/login")
	public String loginPage() {
		return "user/auth/login";
	}

	// Xử lý login
	@PostMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session, Model model) {

		User user = userService.login(email, password);

		if (user == null) {
			model.addAttribute("error", "Email hoặc mật khẩu không đúng");
			return "auth/login";
		}

		// lưu session giống $_SESSION['user']
		session.setAttribute("user", user);

		// nếu là admin -> dashboard
		if (user.getRole() == User.Role.admin) {
			return "redirect:/dashboard";
		}

		// nếu là user -> trang chủ
		return "redirect:/";
	}

	// logout
	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/";
	}

}