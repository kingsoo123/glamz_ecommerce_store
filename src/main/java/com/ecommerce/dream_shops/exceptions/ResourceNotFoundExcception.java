package com.ecommerce.dream_shops.exceptions;

public class ResourceNotFoundExcception extends RuntimeException {
    public ResourceNotFoundExcception(String message){
        super(message);
    }
}
