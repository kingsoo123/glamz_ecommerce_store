package com.ecommerce.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dream_shops.model.CartItems;

public interface CartItemRepository extends JpaRepository<CartItems, Long>{

    void deleteAllByCartId(Long id);
    
}
