//package com.fruitshop.fruit_shop.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//            .authorizeHttpRequests(auth -> auth
//
//                // chỉ ADMIN truy cập
//                .requestMatchers("/admin/**").hasRole("admin")
//
//                // ai cũng truy cập được
//                .requestMatchers("/", "/shop/**", "/css/**", "/js/**", "/images/**").permitAll()
//
//                // các request khác phải login
//                .anyRequest().authenticated()
//            )
//
//            .formLogin(login -> login
//                .loginPage("/login")   // trang login custom
//                .permitAll()
//            )
//
//            .logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout")
//                .permitAll()
//            );
//
//        return http.build();
//    }
//}
//	implementation 'org.springframework.boot:spring-boot-starter-security'
