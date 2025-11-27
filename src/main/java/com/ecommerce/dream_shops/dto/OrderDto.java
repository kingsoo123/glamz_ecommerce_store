package com.ecommerce.dream_shops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.ecommerce.dream_shops.model.OrderItem;

import lombok.Data;

@Data
public class OrderDto {
    private LocalDate orderDate;
    private Long userId;
    private BigDecimal totalAmount;
    private String orderStatus;
    private Set<OrderItem> orderItems = new HashSet<>();
}
