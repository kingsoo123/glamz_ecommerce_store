package com.ecommerce.dream_shops.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Data;

@Data
public class CartDto {
    private Long cartId;
    private Set<CartItemDto> cartitems;
    private BigDecimal totalAMOUNT;
}
