package com.noteflow.noteflow_backend.service;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.repository.CategoryRepository;
import com.noteflow.noteflow_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    // Create new category
    public Category createCategory(Long userId, Category category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if category name already exists for this user
        if (categoryRepository.existsByNameAndUser(category.getName(), user)) {
            throw new RuntimeException("Category name already exists for this user");
        }

        category.setUser(user);
        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByUserId(Long userId) {
        return categoryRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Delete category
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check user ownership
        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Category belongs to different user");
        }

        categoryRepository.deleteById(id);
    }

}
