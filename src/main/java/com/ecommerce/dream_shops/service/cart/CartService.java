package com.ecommerce.dream_shops.service.cart;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Cart;
import com.ecommerce.dream_shops.model.CartItems;
import com.ecommerce.dream_shops.repository.CartItemRepository;
import com.ecommerce.dream_shops.repository.CartRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator =new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
        .orElseThrow(()-> new ResourceNotFoundExcception("No cart found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        //return cartRepository.save(cart);
        return cart;
    }

    
    @Override
    @Transactional
    public void clearCart(Long id) {
       Cart cart = getCart(id);
       cartItemRepository.deleteAllByCartId(id);
       cart.getItems().clear();
       cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
      Cart cart = getCart(id);
      return cart.getItems().stream()
      .map(CartItems::getTotalPrice)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public Long initializeNewCart(){
      Cart newCart = new Cart();
      // Long newCartId = cartIdGenerator.incrementAndGet();
      // newCart.setId(newCartId);
      return cartRepository.save(newCart).getId();
    }


    @Override
    public Cart getCartByUserId(Long userId) {
      return cartRepository.findByUserId(userId);
        
    }
    
}
