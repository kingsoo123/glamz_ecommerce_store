package com.ecommerce.dream_shops.dto;

import java.util.List;

import com.ecommerce.dream_shops.model.Cart;

import lombok.Data;

@Data
public class UserDto {
    
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private Cart cart;
}
