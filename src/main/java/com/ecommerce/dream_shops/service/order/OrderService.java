package com.ecommerce.dream_shops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.dto.OrderDto;
import com.ecommerce.dream_shops.enums.OrderStatus;
import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Cart;
import com.ecommerce.dream_shops.model.Order;
import com.ecommerce.dream_shops.model.OrderItem;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.OrderRepository;
import com.ecommerce.dream_shops.repository.ProductRepository;
import com.ecommerce.dream_shops.service.cart.CartService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class OrderService implements IOrderService{

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;


    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemsList = createOrderItems(order, cart);


        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(calculateTotalAmount(orderItemsList));
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return savedOrder;
    }


    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
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
    public OrderDto getOrder(Long orderId) {
       return orderRepository.findById(orderId).map(this:: convrtToOrderDto)
       .orElseThrow(()-> new ResourceNotFoundExcception("Order not found"));
    }



    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> order = orderRepository.findByUserId(userId);
        return order.stream().map(this::convrtToOrderDto).toList();
    }

    private OrderDto convrtToOrderDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }
    
}
