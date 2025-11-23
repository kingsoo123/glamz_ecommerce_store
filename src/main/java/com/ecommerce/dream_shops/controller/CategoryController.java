package com.ecommerce.dream_shops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.response.ApiResponse;
import com.ecommerce.dream_shops.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final ICategoryService categoryService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
       try {
         List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse("found", categories));
       } catch (Exception e) {
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not found", HttpStatus.INTERNAL_SERVER_ERROR));
       }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name){
        try {
            Category category = categoryService.addCategory(name);
           return ResponseEntity.ok(new ApiResponse("Added", category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not added", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
              Category category = categoryService.getCategoryById(id);
              return ResponseEntity.ok(new ApiResponse("Found", category));
        } catch (ResourceNotFoundExcception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not added", null));
        }
    }



     @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
              Category category = categoryService.getCategoryByName(name);
              return ResponseEntity.ok(new ApiResponse("Found", category));
        } catch (ResourceNotFoundExcception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not added", null));
        }
    }


    @GetMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
              //Category category = categoryService.deleteCategoryById(id); //I will ask chatgpt later about this error message
              categoryService.deleteCategoryById(id);
              return ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundExcception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not found", null));
        }
    }


    @GetMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category){
        try {
              //Category category = categoryService.deleteCategoryById(id); //I will ask chatgpt later about this error message
              Category the_category = categoryService.updateCategory(category,id);
              return ResponseEntity.ok(new ApiResponse("Update success", the_category));
        } catch (ResourceNotFoundExcception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
       .body(new ApiResponse("Not found", null));
        }
    }
}
