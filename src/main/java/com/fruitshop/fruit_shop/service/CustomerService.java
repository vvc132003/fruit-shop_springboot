package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.User;
import com.fruitshop.fruit_shop.entity.User.Role;
import com.fruitshop.fruit_shop.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final UserRepository userRepository;

    public CustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getAllCustomers(Pageable pageable) {
        return userRepository.findByRole(Role.user, pageable);
    }

    public Page<User> searchCustomers(String keyword, Pageable pageable) {
        return userRepository.findByRoleAndFirstNameContainingIgnoreCase(Role.user, keyword, pageable);
    }

}