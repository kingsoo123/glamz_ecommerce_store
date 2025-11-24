package com.ecommerce.dream_shops.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Cart;
import com.ecommerce.dream_shops.model.CartItems;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.CartItemRepository;
import com.ecommerce.dream_shops.repository.CartRepository;
import com.ecommerce.dream_shops.service.product.IProductService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartItemService implements ICartItemService{

    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. get the cart
        //2. get the product
        //3. check if the product already exist
        //4. if yes, increase the quantity with the requested quantity
        //5. if no initiate a new cartitem
        System.out.println("chechchchchch::::::::2");
       Cart cart = cartService.getCart(cartId);
       Product product = productService.getProductById(productId);
       CartItems cartItems = cart.getItems().stream().filter(item-> item.getProduct().getId().equals(productId))
       .findFirst().orElse(new CartItems());

       if(cartItems.getId() == null){
        cartItems.setCart(cart);
        cartItems.setProduct(product);
        cartItems.setQuantity(quantity);
        cartItems.setUnitPrice(product.getPrice());
       }else{
        cartItems.setQuantity(cartItems.getQuantity() + quantity);
       }

       cartItems.setTotalPrice();
       cart.addItems(cartItems);
       cartItemRepository.save(cartItems);
       cartRepository.save(cart);


    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
      Cart cart = cartService.getCart(cartId);
      CartItems itemToRemove = getCartItem(cartId, productId);
      
      
      //.getItems()
      //.stream().filter(item->item.getProduct().getId().equals(productId))
      //.findFirst().orElseThrow(()-> new ResourceNotFoundExcception("Product not found"));// find out about the complains when you used map instead of filter


      cart.removeItem(itemToRemove);
      cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
      Cart cart = cartService.getCart(cartId);
            cart.getItems().stream()
            .filter(item->item.getProduct().getId().equals(productId))
            .findFirst().ifPresent(item->{
                item.setQuantity(quantity);
                item.setUnitPrice(item.getProduct().getPrice());
                item.setTotalPrice();
            });

            BigDecimal totalAmount = cart.getItems().stream().map(CartItems::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalAmount(totalAmount);
            cartRepository.save(cart);
    }



    @Override
    public CartItems getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
      .stream().filter(item->item.getProduct().getId().equals(productId))
      .findFirst().orElseThrow(()-> new ResourceNotFoundExcception("Product not found"));
    }
    
}
