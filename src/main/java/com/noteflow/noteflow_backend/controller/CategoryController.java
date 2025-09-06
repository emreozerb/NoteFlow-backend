package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Create new category
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createCategory(@PathVariable Long userId, @Valid @RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(userId, category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all categories for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCategoriesByUser(@PathVariable Long userId) {
        try {
            List<Category> categories = categoryService.getCategoriesByUserId(userId);
            return ResponseEntity.ok(categories);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all categories (admin only)
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete category
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, @PathVariable Long userId) {
        try {
            categoryService.deleteCategory(id, userId);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
