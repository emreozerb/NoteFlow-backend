package com.noteflow.noteflow_backend.service;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategoriesByUser(User user) {
        return categoryRepository.findByUser(user);
    }

    public Category getCategoryById(Long id, User user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to category");
        }

        return category;
    }

    public Category createCategory(String name, User user) {
        // Check if category name already exists for this user
        List<Category> existingCategories = categoryRepository.findByUser(user);
        boolean nameExists = existingCategories.stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase(name));

        if (nameExists) {
            throw new RuntimeException("Category name already exists");
        }

        Category category = new Category();
        category.setName(name);
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, String name, User user) {
        Category category = getCategoryById(id, user);

        // Check if new name already exists for this user (excluding current category)
        List<Category> existingCategories = categoryRepository.findByUser(user);
        boolean nameExists = existingCategories.stream()
                .anyMatch(cat -> !cat.getId().equals(id) && cat.getName().equalsIgnoreCase(name));

        if (nameExists) {
            throw new RuntimeException("Category name already exists");
        }

        category.setName(name);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id, User user) {
        Category category = getCategoryById(id, user);
        categoryRepository.delete(category);
    }
}