package com.ecommerce.dream_shops.service.order;

import com.ecommerce.dream_shops.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);
}
