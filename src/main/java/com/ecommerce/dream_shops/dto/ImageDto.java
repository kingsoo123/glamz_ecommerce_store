package com.ecommerce.dream_shops.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {

    private Long id;
    private String fileName;
    private String downloadUrl;
    
}
