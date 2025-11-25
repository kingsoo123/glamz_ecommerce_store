package com.ecommerce.dream_shops.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
}
