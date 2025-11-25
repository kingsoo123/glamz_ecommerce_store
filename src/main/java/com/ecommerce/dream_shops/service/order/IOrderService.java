package com.ecommerce.dream_shops.service.order;

import java.util.List;

import com.ecommerce.dream_shops.dto.OrderDto;
import com.ecommerce.dream_shops.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
