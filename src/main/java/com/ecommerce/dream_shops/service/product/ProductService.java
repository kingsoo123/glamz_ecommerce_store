package com.ecommerce.dream_shops.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.CategoryRepository;
import com.ecommerce.dream_shops.repository.ProductRepository;
import com.ecommerce.dream_shops.request.AddProductRequest;
import com.ecommerce.dream_shops.request.UpdateProductRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
        .orElseGet(()->{
            Category newCategory = Category.builder()
            .name(request.getCategory().getName())
            .build();
            return categoryRepository.save(newCategory);
        });
      request.setCategory(category);
      return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category){

        return Product.builder()
        .name(request.getName())
        .brand(request.getBrand())
        .price(request.getPrice())
        .inventory(request.getInventory())
        .description(request.getDescription())
        .category(category)
        .build();

        
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
        .orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
        .ifPresentOrElse(productRepository::delete, 
            ()-> new ResourceNotFoundExcception("Product not found"));
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
        .map(existingProduct -> updateExistingProduct(existingProduct, request))
        .map(productRepository::save)
        .orElseThrow(()-> new ResourceNotFoundExcception("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        Category category = categoryRepository.findByName(request.getCategory().getName());
        return Product.builder()
         .name(request.getName())
        .brand(request.getBrand())
        .price(request.getPrice())
        .inventory(request.getInventory())
        .description(request.getDescription())
        .category(category)
        .build();

    }

    @Override
    public List<Product> getAllProducts() {
       return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
       return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    

    @Override
    public List<Product> getProductsByName(String name) {
      return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
       return productRepository.findByBrandAndName(brand, name);// why can't catch exception with this query?;
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
       return productRepository.countByBrandAndName(brand, name);
    }
    
}
