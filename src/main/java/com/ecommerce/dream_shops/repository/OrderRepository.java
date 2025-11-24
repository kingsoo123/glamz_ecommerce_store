package com.ecommerce.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dream_shops.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

    
}
