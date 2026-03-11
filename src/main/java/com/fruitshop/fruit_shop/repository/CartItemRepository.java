package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.CartItem;
import com.fruitshop.fruit_shop.entity.Cart;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	List<CartItem> findByCart(Cart cart);

	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

	@Query("""
			    SELECT COALESCE(SUM(ci.quantity),0)
			    FROM CartItem ci
			    WHERE ci.cart.user.id = :userId
			""")
	Integer countByUser(@Param("userId") Integer userId);

	List<CartItem> findByCart_User(User user);

}