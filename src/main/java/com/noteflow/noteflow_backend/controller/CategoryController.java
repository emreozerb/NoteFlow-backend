package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Category> categories = categoryService.getCategoriesByUser(user);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Category category = categoryService.getCategoryById(id, user);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryRequest request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Category category = categoryService.createCategory(request.getName(), user);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CreateCategoryRequest request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Category category = categoryService.updateCategory(id, request.getName(), user);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            categoryService.deleteCategory(id, user);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    public static class CreateCategoryRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}