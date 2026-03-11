package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.entity.User.Role;
import com.fruitshop.fruit_shop.entity.User.Status;
import com.fruitshop.fruit_shop.repository.OrderRepository;
import com.fruitshop.fruit_shop.repository.ProductRepository;
import com.fruitshop.fruit_shop.service.MailService;
import com.fruitshop.fruit_shop.service.PasswordResetService;
import com.fruitshop.fruit_shop.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;
	private final PasswordResetService passwordResetService;

	public AuthController(UserService userService, PasswordEncoder passwordEncoder, MailService mailService,
			PasswordResetService passwordResetService) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.passwordResetService = passwordResetService;
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
			return "user/auth/login";
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

	@GetMapping("/changePassword")
	public String changePassword(Model model) {
		return "user/auth/changePassword";
	}

	@PostMapping("/changePassword")
	public String changePasswordPost(@RequestParam("current_password") String currentPassword,
			@RequestParam("new_password") String newPassword, @RequestParam("confirm_password") String confirmPassword,
			HttpSession session, Model model) {

		// lấy user từ session
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		// kiểm tra mật khẩu cũ
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			model.addAttribute("error", "Mật khẩu hiện tại không đúng");
			return "user/auth/changePassword";
		}

		// kiểm tra confirm password
		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("error", "Mật khẩu xác nhận không khớp");
			return "user/auth/changePassword";
		}

		// encode mật khẩu mới
		user.setPassword(passwordEncoder.encode(newPassword));

		// update database
		userService.save(user);

		model.addAttribute("success", "Cập nhật mật khẩu thành công");

		return "user/auth/changePassword";
	}

	@GetMapping("/forgotPassword")
	public String forgotPassword(Model model) {
		return "user/auth/forgotPassword";
	}

	@PostMapping("/send")
	public String sendResetCode(@RequestParam("email") String email, Model model) {

		User user = userService.findByEmail(email).orElse(null);

		if (user == null) {
			model.addAttribute("error", "Email không tồn tại!");
			return "user/auth/forgotPassword";
		}

		String code = String.valueOf((int) (Math.random() * 900000) + 100000);

		passwordResetService.createCode(user, code);

		mailService.sendResetPasswordMail(email, code);

		model.addAttribute("success", "Đã gửi mã xác nhận đến email!");
		return "user/auth/resetPassword";
	}

	@GetMapping("/resetPassword")
	public String resetPassword(Model model) {
		return "user/auth/resetPassword";
	}

	@PostMapping("/resetStore")
	public String resetPassword(@RequestParam("code") String code, @RequestParam("password") String password,
			@RequestParam("password_confirm") String password_confirm, Model model) {

		if (!password.equals(password_confirm)) {
			model.addAttribute("error", "Mật khẩu không khớp!");
			return "user/auth/resetPassword";
		}

		boolean success = passwordResetService.resetPassword(code, password);
		if (!success) {
			model.addAttribute("error", "Mã xác nhận không hợp lệ hoặc đã hết hạn!");
			return "user/auth/resetPassword";
		}

		model.addAttribute("success", "Đổi mật khẩu thành công!");
		return "user/auth/resetPassword";
	}

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "user/auth/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user, @RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword, Model model) {

		if (!password.equals(confirmPassword)) {
			model.addAttribute("error", "Mật khẩu không khớp");
			return "user/auth/register";
		}

		if (userService.existsByEmail(user.getEmail())) {
			model.addAttribute("error", "Email đã tồn tại!");
			return "user/auth/register";
		}

		user.setPassword(passwordEncoder.encode(password));
		user.setCreatedAt(LocalDateTime.now());
		user.setRole(Role.user);
		user.setStatus(Status.active);
		userService.save(user);
		model.addAttribute("success", "Đăng ký thành công!");

		return "redirect:/auth/login";
	}

	@GetMapping("/edit")
	public String editProfile(HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/auth/login";
		}
		model.addAttribute("user", user);

		return "admin/profile/edit";
	}

	@PostMapping("/update")
	public String updateProfile(HttpSession session,

			@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("email") String email, @RequestParam("phone") String phone,
			@RequestParam("address") String address,

			@RequestParam(value = "avatar", required = false) MultipartFile avatar,

			RedirectAttributes redirectAttributes) throws Exception {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		userService.updateProfile(user.getId(), firstName, lastName, email, phone, address, avatar);

		redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");

		return "redirect:/auth/edit";
	}

	@GetMapping("/password")
	public String password(HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/auth/login";
		}
		model.addAttribute("user", user);

		return "admin/profile/password";
	}

	@PostMapping("/updatePassword")
	public String updatePassword(HttpSession session,

			@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword,

			RedirectAttributes redirectAttributes) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login";
		}

		try {

			userService.changePassword(user.getId(), currentPassword, newPassword, confirmPassword);

			redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");

		} catch (RuntimeException e) {

			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:/auth/password";
	}
}