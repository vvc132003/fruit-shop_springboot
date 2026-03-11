package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Cart;
import com.fruitshop.fruit_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(User user);

}