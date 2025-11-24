package com.ecommerce.dream_shops.service.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Cart;
import com.ecommerce.dream_shops.model.Order;
import com.ecommerce.dream_shops.model.OrderItem;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.OrderRepository;
import com.ecommerce.dream_shops.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.val;


@Service
@AllArgsConstructor
public class OrderService implements IOrderService{

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;


    @Override
    public Order placeOrder(Long userId) {
        return null;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem ->{
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return OrderItem.builder()
            .quantity(cartItem.getQuantity())
            .price(cartItem.getUnitPrice())
            .order(order)
            .product(product)
            .build();
        }).toList();
    }



    //what is the difference between * and .multiply()
    //see how you can convert method reference to normal lamda method
    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream()
        .map(item-> item.getPrice()
        .multiply(new BigDecimal(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrder(Long orderId) {
       return orderRepository.findById(orderId)
       .orElseThrow(()-> new ResourceNotFoundExcception("Order not found"));
    }
    
}
