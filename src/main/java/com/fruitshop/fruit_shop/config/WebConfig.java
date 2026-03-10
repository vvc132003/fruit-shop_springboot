package com.fruitshop.fruit_shop.config;

import com.fruitshop.fruit_shop.interceptor.AdminInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	// Khai báo Bean cho Interceptor
	@Bean
	public AdminInterceptor adminInterceptor() {
		return new AdminInterceptor();
	}

	// Đăng ký Interceptor
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(adminInterceptor()).addPathPatterns("/**") // áp dụng cho tất cả request
				.excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**", "/login", "/register", "/");
	}
}