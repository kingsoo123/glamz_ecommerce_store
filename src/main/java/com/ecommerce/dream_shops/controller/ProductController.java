package com.ecommerce.dream_shops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dream_shops.dto.ProductDto;
import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.request.AddProductRequest;
import com.ecommerce.dream_shops.request.UpdateProductRequest;
import com.ecommerce.dream_shops.response.ApiResponse;
import com.ecommerce.dream_shops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final IProductService productService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        try {
            List<Product> product = productService.getAllProducts();
            List<ProductDto> convertedProducts = productService.getConvertedProducts(product);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
            
        } catch (ResourceNotFoundExcception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not found", null));
        } 
    }


     @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getAllProductsById(@PathVariable Long productId){
        try {
            Product product = productService.getProductById(productId);
            var productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Products found", productDto));
            
        } catch (ResourceNotFoundExcception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not found", null));
        } 
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
        try {
             Product product = productService.addProduct(request);
              return ResponseEntity.ok(new ApiResponse("Add product success", product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not found", null));
        } 
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId){
        try {
             Product product = productService.updateProduct(request, productId);
        return ResponseEntity.ok(new ApiResponse("Product updated", product));
        } catch (ResourceNotFoundExcception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse(e.getMessage(), null));
        }
    }

     @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        try {
             productService.deleteProductById(productId);
        return ResponseEntity.ok(new ApiResponse("Delete updated", productId));
        } catch (ResourceNotFoundExcception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
             List<Product> products = productService.getProductsByBrandAndName(brand, name);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No product found", null));
        }

        return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand){
        try {
             List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No product found", null));
        }

        return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


     @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name){
        try {
             List<Product> products = productService.getProductsByName(name);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No product found", null));
        }

        return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


     @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand){
        try {
             List<Product> products = productService.getProductsByBrand(brand);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No product found", null));
        }

        return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String category){
        try {
             List<Product> products = productService.getProductsByCategory(category);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No product found", null));
        }

        return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


     @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
             var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}
