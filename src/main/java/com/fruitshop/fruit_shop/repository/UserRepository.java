package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.entity.User.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByRoleAndFirstNameContainingIgnoreCase(Role role, String keyword, Pageable pageable);
    User findByEmail(String email);


}