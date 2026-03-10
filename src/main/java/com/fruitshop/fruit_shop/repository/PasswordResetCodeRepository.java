package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.PasswordResetCode;
import com.fruitshop.fruit_shop.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Integer> {
	Optional<PasswordResetCode> findByCodeAndExpiredAtAfter(
            String code, LocalDateTime now);

    void deleteByUser(User user);

}