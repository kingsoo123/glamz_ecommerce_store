package com.ecommerce.dream_shops.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
    private Long cartId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
