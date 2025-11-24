package com.ecommerce.dream_shops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.response.ApiResponse;
import com.ecommerce.dream_shops.service.cart.ICartItemService;
import com.ecommerce.dream_shops.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;


    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId, 
        @RequestParam Long productId, 
        @RequestParam Integer quantity){

           
            try {
                 if(cartId == null){
                cartId = cartService.initializeNewCart();
            }
            cartItemService.addItemToCart(cartId, productId, quantity);
            
            return ResponseEntity.ok(new ApiResponse("Add item success", null));
            } catch (ResourceNotFoundExcception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
          
        }

        @DeleteMapping("/{cartId}/item/{productId}/remove")
        public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId){
            try {
                cartItemService.removeItemFromCart(cartId, productId);
                return ResponseEntity.ok(new ApiResponse( "Removed item success", null));
                
            } catch (ResourceNotFoundExcception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }


        @PutMapping("/cart/{cartId}/item/{productId}/update")
        public ResponseEntity<ApiResponse>  updateItemQuantity(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam Integer quantity){
            try {
                System.out.println("chechchchchch::::::::1");
                cartItemService.updateItemQuantity(cartId, productId, quantity);
                return ResponseEntity.ok(new ApiResponse("Update item success", null));
            } catch (ResourceNotFoundExcception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }
}
