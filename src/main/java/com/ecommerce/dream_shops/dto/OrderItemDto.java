package com.ecommerce.dream_shops.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long product_id;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
