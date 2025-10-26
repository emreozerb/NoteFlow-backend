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

import com.noteflow.noteflow_backend.dto.request.category.CreateCategoryRequestDTO;
import com.noteflow.noteflow_backend.dto.response.CategoryDTO;
import com.noteflow.noteflow_backend.mapper.CategoryMapper;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Category> categories = categoryService.getCategoriesByUser(user);
        List<CategoryDTO> categoryDTOs = categoryMapper.toDTOList(categories);
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Category category = categoryService.getCategoryById(id, user);
        CategoryDTO categoryDTO = categoryMapper.toDTO(category);
        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryRequestDTO request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Category category = categoryService.createCategory(request.getName(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CreateCategoryRequestDTO request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Category category = categoryService.updateCategory(id, request.getName(), user);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        categoryService.deleteCategory(id, user);
        return ResponseEntity.ok("Category deleted successfully");
    }

};