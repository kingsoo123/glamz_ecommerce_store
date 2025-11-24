package com.ecommerce.dream_shops.service.cart;

import com.ecommerce.dream_shops.model.CartItems;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItems getCartItem(Long cartId, Long productId);
}
