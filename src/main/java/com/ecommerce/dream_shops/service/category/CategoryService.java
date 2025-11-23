package com.ecommerce.dream_shops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.exceptions.AlreadyExistsException;
import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.repository.CategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundExcception("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
      return categoryRepository.findByName(name);//I will ask chatgpt why the chain method is not found
      //.orElseThrow(()-> new ResourceNotFoundExcception("Category not found!"));
    }

    @Override
    public List<Category> getAllCategories() {
       return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        //build new category
        //save the category
       return Optional.ofNullable(category).filter((c)-> !categoryRepository.existsByName(c.getName()))
       .map(categoryRepository::save)
       .orElseThrow(()-> new AlreadyExistsException(category.getName() + " Already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        // find by id
        // change the name
        // save
        // else throw error
       return Optional.ofNullable(getCategoryById(id)).map(oldCategory-> {
        oldCategory.setName(category.getName());
        return categoryRepository.save(oldCategory);// why return repository.save()
       }).orElseThrow(()-> new ResourceNotFoundExcception("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
       categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, ()-> new ResourceNotFoundExcception("Category not found!"));
    }
    
}
