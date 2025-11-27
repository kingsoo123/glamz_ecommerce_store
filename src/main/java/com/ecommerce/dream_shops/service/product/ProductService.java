package com.ecommerce.dream_shops.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.dto.ImageDto;
import com.ecommerce.dream_shops.dto.ProductDto;
import com.ecommerce.dream_shops.exceptions.AlreadyExistsException;
import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.model.Image;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.CategoryRepository;
import com.ecommerce.dream_shops.repository.ImageRepository;
import com.ecommerce.dream_shops.repository.ProductRepository;
import com.ecommerce.dream_shops.request.AddProductRequest;
import com.ecommerce.dream_shops.request.UpdateProductRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {

if(productExist(request.getName(), request.getBrand())){
    throw new AlreadyExistsException("Product already exists in database");
}

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



    private boolean productExist(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
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


    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image->modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
    
}
