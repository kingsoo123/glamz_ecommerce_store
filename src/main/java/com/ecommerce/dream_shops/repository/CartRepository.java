package com.ecommerce.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dream_shops.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);

    
}