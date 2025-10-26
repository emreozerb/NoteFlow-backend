package com.noteflow.noteflow_backend.mapper;

import com.noteflow.noteflow_backend.dto.request.category.CreateCategoryRequestDTO;
import com.noteflow.noteflow_backend.dto.response.CategoryDTO;
import com.noteflow.noteflow_backend.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null)
            return null;

        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }

    public List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Category toEntity(CreateCategoryRequestDTO dto, User user) {
        if (dto == null)
            return null;

        Category category = new Category();
        category.setName(dto.getName());
        category.setUser(user);
        return category;
    }
}
