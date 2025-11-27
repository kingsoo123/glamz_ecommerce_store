package com.ecommerce.dream_shops.request;


import java.math.BigDecimal;

import com.ecommerce.dream_shops.model.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductRequest {

    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
