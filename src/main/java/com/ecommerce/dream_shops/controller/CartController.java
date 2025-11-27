package com.ecommerce.dream_shops.controller;



import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Cart;
import com.ecommerce.dream_shops.response.ApiResponse;
import com.ecommerce.dream_shops.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final ICartService cartService;


    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){
        try {
             Cart cart = cartService.getCart(cartId);
             return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundExcception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Not found", null));
        }
       
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse>  clearCart(@PathVariable Long cartId){
        try {
              cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Clear cart successfully", null));
        } catch (ResourceNotFoundExcception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));

        }
      
    }


    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
             BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(new ApiResponse("Total price",  totalPrice));
        } catch (ResourceNotFoundExcception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
       
    }
}
