package com.ecommerce.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dream_shops.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String request);

    User findByEmail(String email);
    
}
