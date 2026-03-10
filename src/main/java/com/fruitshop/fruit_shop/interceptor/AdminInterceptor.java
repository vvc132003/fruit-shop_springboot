package com.fruitshop.fruit_shop.interceptor;

import com.fruitshop.fruit_shop.annotation.AdminOnly;
import com.fruitshop.fruit_shop.entity.User;
import jakarta.servlet.http.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod method = (HandlerMethod) handler;

		boolean isAdminOnly = method.hasMethodAnnotation(AdminOnly.class)
				|| method.getBeanType().isAnnotationPresent(AdminOnly.class);

		if (isAdminOnly) {

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if (user == null || user.getRole() != User.Role.admin) {
				response.sendRedirect("/");
				return false;
			}
		}

		return true;
	}
}